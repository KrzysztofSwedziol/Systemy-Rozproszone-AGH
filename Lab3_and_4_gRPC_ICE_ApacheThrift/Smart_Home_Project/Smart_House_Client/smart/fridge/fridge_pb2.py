# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# NO CHECKED-IN PROTOBUF GENCODE
# source: smart/fridge/fridge.proto
# Protobuf Python Version: 5.29.0
"""Generated protocol buffer code."""
from google.protobuf import descriptor as _descriptor
from google.protobuf import descriptor_pool as _descriptor_pool
from google.protobuf import runtime_version as _runtime_version
from google.protobuf import symbol_database as _symbol_database
from google.protobuf.internal import builder as _builder
_runtime_version.ValidateProtobufRuntimeVersion(
    _runtime_version.Domain.PUBLIC,
    5,
    29,
    0,
    '',
    'smart/fridge/fridge.proto'
)
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()


from google.protobuf import empty_pb2 as google_dot_protobuf_dot_empty__pb2


DESCRIPTOR = _descriptor_pool.Default().AddSerializedFile(b'\n\x19smart/fridge/fridge.proto\x12\x0csmart.fridge\x1a\x1bgoogle/protobuf/empty.proto\"&\n\x04Item\x12\x0c\n\x04name\x18\x01 \x01(\t\x12\x10\n\x08quantity\x18\x02 \x01(\x05\"\xb0\x01\n\x0b\x46ridgeState\x12\x1a\n\x12\x66ridge_temperature\x18\x01 \x01(\x02\x12\x1b\n\x13\x66reezer_temperature\x18\x02 \x01(\x02\x12\x0f\n\x07lightOn\x18\x03 \x01(\x08\x12*\n\x0e\x66ridgeContents\x18\x04 \x03(\x0b\x32\x12.smart.fridge.Item\x12+\n\x0f\x66reezerContents\x18\x05 \x03(\x0b\x32\x12.smart.fridge.Item\"k\n\x10\x43ontentsResponse\x12*\n\x0e\x66ridgeContents\x18\x01 \x03(\x0b\x32\x12.smart.fridge.Item\x12+\n\x0f\x66reezerContents\x18\x02 \x03(\x0b\x32\x12.smart.fridge.Item\" \n\rLightResponse\x12\x0f\n\x07lightOn\x18\x01 \x01(\x08\"/\n\x12TemperatureRequest\x12\x19\n\x11targetTemperature\x18\x01 \x01(\x02\"*\n\x13TemperatureResponse\x12\x13\n\x0btemperature\x18\x01 \x01(\x02\"\x16\n\x03\x41\x63k\x12\x0f\n\x07message\x18\x01 \x01(\t2\xd1\x06\n\x06\x46ridge\x12=\n\x08GetState\x12\x16.google.protobuf.Empty\x1a\x19.smart.fridge.FridgeState\x12Q\n\x14GetFridgeTemperature\x12\x16.google.protobuf.Empty\x1a!.smart.fridge.TemperatureResponse\x12R\n\x15GetFreezerTemperature\x12\x16.google.protobuf.Empty\x1a!.smart.fridge.TemperatureResponse\x12K\n\x14SetFridgeTemperature\x12 .smart.fridge.TemperatureRequest\x1a\x11.smart.fridge.Ack\x12L\n\x15SetFreezerTemperature\x12 .smart.fridge.TemperatureRequest\x1a\x11.smart.fridge.Ack\x12\x45\n\x0eGetLightStatus\x12\x16.google.protobuf.Empty\x1a\x1b.smart.fridge.LightResponse\x12\x38\n\x0bToggleLight\x12\x16.google.protobuf.Empty\x1a\x11.smart.fridge.Ack\x12\x45\n\x0bGetContents\x12\x16.google.protobuf.Empty\x1a\x1e.smart.fridge.ContentsResponse\x12;\n\x12\x41\x64\x64ProductToFridge\x12\x12.smart.fridge.Item\x1a\x11.smart.fridge.Ack\x12<\n\x13\x41\x64\x64ProductToFreezer\x12\x12.smart.fridge.Item\x1a\x11.smart.fridge.Ack\x12@\n\x17RemoveProductFromFridge\x12\x12.smart.fridge.Item\x1a\x11.smart.fridge.Ack\x12\x41\n\x18RemoveProductFromFreezer\x12\x12.smart.fridge.Item\x1a\x11.smart.fridge.Ackb\x06proto3')

_globals = globals()
_builder.BuildMessageAndEnumDescriptors(DESCRIPTOR, _globals)
_builder.BuildTopDescriptorsAndMessages(DESCRIPTOR, 'smart.fridge.fridge_pb2', _globals)
if not _descriptor._USE_C_DESCRIPTORS:
  DESCRIPTOR._loaded_options = None
  _globals['_ITEM']._serialized_start=72
  _globals['_ITEM']._serialized_end=110
  _globals['_FRIDGESTATE']._serialized_start=113
  _globals['_FRIDGESTATE']._serialized_end=289
  _globals['_CONTENTSRESPONSE']._serialized_start=291
  _globals['_CONTENTSRESPONSE']._serialized_end=398
  _globals['_LIGHTRESPONSE']._serialized_start=400
  _globals['_LIGHTRESPONSE']._serialized_end=432
  _globals['_TEMPERATUREREQUEST']._serialized_start=434
  _globals['_TEMPERATUREREQUEST']._serialized_end=481
  _globals['_TEMPERATURERESPONSE']._serialized_start=483
  _globals['_TEMPERATURERESPONSE']._serialized_end=525
  _globals['_ACK']._serialized_start=527
  _globals['_ACK']._serialized_end=549
  _globals['_FRIDGE']._serialized_start=552
  _globals['_FRIDGE']._serialized_end=1401
# @@protoc_insertion_point(module_scope)
