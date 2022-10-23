package corgitaco.corgilib.serialization.codec;

import java.util.Optional;

public record Wrapped<T>(Optional<String> id, T value) {
}