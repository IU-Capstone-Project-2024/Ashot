import argparse
import os
import numpy as np
import onnxruntime
import torch
import torchvision.transforms as transforms
import torch.nn.functional as F
from PIL import Image

from testing import find_similar

"""
Testing 1-IMG ONNX model
"""


def extract_features(model):
	images_dir = os.path.join(os.getcwd(), "datasets", "custom", "jpg")

	imgs = [Image.open(os.path.join(images_dir, img)) for img in os.listdir(images_dir)]

	t = transforms.Compose([
		transforms.Resize((500, 500)),
		transforms.ToTensor(),
		transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225])
	])

	imgs = [t(img).unsqueeze(0).numpy() for img in imgs]

	session = onnxruntime.InferenceSession(f"./checkpoints/{model}")
	out = [session.run(None, {"l_tensor_": img})[0] for img in imgs]
	img_feats = torch.tensor(np.vstack(out))
	X = F.normalize(img_feats, p=2, dim=1)
	return X


def main(qimg, model="CVNet50.onnx"):
	X = extract_features(model)
	find_similar(qimg, X, images_dir=os.path.join(os.getcwd(), "datasets/custom/jpg"))


if __name__ == "__main__":
	parser = argparse.ArgumentParser(description='Find Similar Images')
	parser.add_argument('qimg', type=str, help='Query Image')
	args = parser.parse_args()
	main(args.qimg)
