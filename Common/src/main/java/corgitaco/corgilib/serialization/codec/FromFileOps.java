package corgitaco.corgilib.serialization.codec;


import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.ListBuilder;
import com.mojang.serialization.RecordBuilder;
import net.minecraft.resources.DelegatingOps;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FromFileOps<T> extends DelegatingOps<T> {

    private final Access access;

    public FromFileOps(DynamicOps<T> dynamicOps, Access access) {
        super(dynamicOps);
        this.access = access;
    }

    @Override
    public @NotNull ListBuilder<T> listBuilder() {
        return this.delegate.listBuilder();
    }

    @Override
    public @NotNull RecordBuilder<T> mapBuilder() {
        return this.delegate.mapBuilder();
    }

    public <E> Map<String, E> getAccess(String s) {
        return access.get(s);
    }

    public static final class Access {
        private final Map<String, Map<String, ?>> registry = new HashMap<>();

        public Map<String, Map<String, ?>> registry() {
            return registry;
        }

        public <T> Map<String, T> get(String s) {
           return (Map<String, T>) registry.computeIfAbsent(s, key -> new HashMap<>());
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (Access) obj;
            return Objects.equals(this.registry, that.registry);
        }

        @Override
        public int hashCode() {
            return Objects.hash(registry);
        }

        @Override
        public String toString() {
            return "Access[" +
                "registry=" + registry + ']';
        }
    }
}