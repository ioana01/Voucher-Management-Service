import java.util.*;

public class ArrayMap<K, V> extends AbstractMap<K, V> {
//    public List<ArrayMapEntry<K, List<V>>> list;
    public List<ArrayMapEntry<K,V>> list = null;

    public ArrayMap() {
        list = new ArrayList<>(); //alocam memorie pentru lista
    }

    public class ArrayMapEntry<K, V> implements Map.Entry<K,V>
    {
        private K key;
        private V value;

        public ArrayMapEntry(K key, V val) {
            this.key = key;
            this.value = val;
        }

        @Override
        public K getKey() {
            return this.key;
        }

        @Override
        public V getValue() {
            if(this.value != null)
                return this.value;

            return null;
        }

        @Override
        public V setValue(V v) {
            V oldValue = this.value;

            this.value = v; //setam noua valoare si o returnam pe cea veche

            return oldValue;
        }

        public void setKey(K k) {
            this.key = k;
        }

        public String toString() {
            String result = null;

            result += "The key is " + this.key + " and the value is " + this.value;

            return result;
        }

        public boolean equals(Object o) {
            if(o == null)
                return false;

            if (!(o instanceof ArrayMapEntry))
                return false;

            ArrayMapEntry<K, V> e = (ArrayMapEntry) o; //daca cele 2 obiecte au aceeasi cheie si aceeasi valoare returnam true altfel false
            if((e.getKey() == this.key && e.getValue() == this.value))
                return true;

            return false;
        }

        public int hashCode() {
            return (this.getKey() == null ? 0 : this.getKey().hashCode()) ^
                    (this.getValue() == null ? 0 : this.getValue().hashCode());
        }
    }

    public V put(K key, V value)
    {
        V val = null;
        int i, ok = 0;

        for(i=0; i < list.size(); i++)
            if(key.equals(list.get(i).getKey())) //cautam in lista intrarea cu cheia egala cu cea data in antet
            {
                val = list.get(i).getValue();
                list.get(i).setValue(value);
                ok = 1;
            }

        if(ok == 0) //daca variabla ramane 0 inseamna ca nu a fost gasita cheia si cream o noua intrare in lista pentru aceasta
            list.add(new ArrayMapEntry(key, value));

        return val;
    }

    public boolean containsKey(Object key) {
        if(key == null)
            return false;

        for(int i = 0; i < list.size(); i++) //daca vreuna dintre intrarile din lista are cheia precizata returnam true, altfel false
            if(list.get(i).getKey() == (K) key)
                return true;

        return false;
    }

    public V get(Object key)
    {
        if(containsKey(key))
            for(int i = 0; i < list.size(); i++)
                if(list.get(i).getKey() == (K) key)
                    return list.get(i).getValue();

         return null;
    }

    public int size() {
        return this.list.size();
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet()
    {
        Set<Map.Entry<K, V>> s = new HashSet<>();

        for(int z = 0; z< this.list.size(); z++) //cream un HashSet cu toate perechile cheie-valoare din lista
            s.add(new ArrayMapEntry<>(this.list.get(z).getKey(), (V)this.list.get(z).getValue()));

        return s;
    }

}
