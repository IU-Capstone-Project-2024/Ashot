import os
import re
import torch

from SuperGlobal.model.CVNet_Rerank_model import CVNet_Rerank
from SuperGlobal.config import cfg
import SuperGlobal.core.checkpoint as checkpoint

from ModelWrapper import EmbeddingExtractor


def load_model(weights, model_depth):
	"""
	Loads model from checkpoint and returns wrapped model for extracting to ONNX.
	"""
	# TODO check model params
	model = CVNet_Rerank(model_depth, cfg.MODEL.HEADS.REDUCTION_DIM, cfg.SupG.relup).cpu()
	model_checkpoint = os.path.join(os.getcwd(), weights)
	checkpoint.load_checkpoint(model_checkpoint, model)
	wrapped_model = EmbeddingExtractor(model)
	return wrapped_model


def exctract(weights, input_size=(500, 500)):
	model_depth = int(re.search(r"R(\d+)(\.pyth)", weights).group(1))
	model = load_model(weights, model_depth)

	# TODO cuda support
	dummy_input = torch.randn(1, 3, input_size[0], input_size[1]).cpu()

	prog = torch.onnx.dynamo_export(model, dummy_input)
	prog.save(f"checkpoints/CVNet{model_depth}.onnx")


def test_tracing():
	from torch.fx import symbolic_trace

	model = load_model("CVPR2022_CVNet_R50.pyth")
	traced_model = symbolic_trace(model)
	print(traced_model.graph)


if __name__ == "__main__":
	exctract("checkpoints/CVPR2022_CVNet_R50.pyth")
