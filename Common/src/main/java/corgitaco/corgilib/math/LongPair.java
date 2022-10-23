package corgitaco.corgilib.math;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class LongPair {

    private final long val1;
    private final long val2;

    public LongPair(long val1, long val2) {
        this.val1 = val1;
        this.val2 = val2;
    }

    public long getVal1() {
        return val1;
    }

    public long getVal2() {
        return val2;
    }

    public boolean isInBetween(long l) {
        return l >= this.val1 && l <= this.val2;
    }

    public static Codec<LongPair> createLongPairCodec(String val1Name, String val2Name) {
        return RecordCodecBuilder.create(builder -> {
            return builder.group(Codec.LONG.fieldOf(val1Name).forGetter(longPair -> longPair.val1), Codec.LONG.fieldOf(val2Name).forGetter(longPair -> longPair.val2)).apply(builder, LongPair::new);
        });
    }

    @Override
    public String toString() {
        return val1 + " - " + val2;
    }
}
