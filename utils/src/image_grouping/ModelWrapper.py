import torch
from SuperGlobal.config import cfg


class EmbeddingExtractor(torch.nn.Module):
	def __init__(self, original_model):
		super(EmbeddingExtractor, self).__init__()
		self.model = original_model

	def forward(self, tensor):
		desc = self.model.extract_global_descriptor(tensor, True, True, True, cfg.TEST.SCALE_LIST)
		return desc.detach().cpu()
