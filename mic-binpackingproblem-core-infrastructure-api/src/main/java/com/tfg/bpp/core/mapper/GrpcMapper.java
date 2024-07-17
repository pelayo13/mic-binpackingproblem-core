package com.tfg.bpp.core.mapper;

import com.google.protobuf.DoubleValue;
import com.google.protobuf.Int32Value;
import com.tfg.bpp.core.config.CentralMapperConfig;
import java.util.Optional;
import org.mapstruct.Mapper;

/** Maps from grpc object to java object. */
@Mapper(config = CentralMapperConfig.class)
public interface GrpcMapper {

  /**
   * Maps Integer to com.google.protobuf.Int32Value.
   *
   * @param value the Long to be parsed by its value.
   * @return Int32Value
   */
  default Int32Value intToInt32(final Integer value) {
    return Optional.ofNullable(value).map(Int32Value::of).orElse(null);
  }

  /**
   * Map com.google.protobuf.Int32Value to Integer.
   *
   * @param value the Int32Value to be parsed by its value.
   * @return Integer
   */
  default Integer int32ToInt(final Int32Value value) {
    return Optional.ofNullable(value).map(Int32Value::getValue).orElse(null);
  }

  /**
   * Maps com.google.protobuf.DoubleValue to Double.
   *
   * @param value the DoubleValue to be parsed by its value.
   * @return Double
   */
  default Double doubleValueToDouble(final DoubleValue value) {
    return value.getValue();
  }

  /**
   * Maps Double to com.google.protobuf.DoubleValue.
   *
   * @param value the Double to be parsed by its value.
   * @return DoubleValue
   */
  default DoubleValue doubleToDoubleValue(final Double value) {
    return DoubleValue.newBuilder().setValue(value).build();
  }
}
