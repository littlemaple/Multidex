package imagination.ga.marsh;

/**
 * Created by 44260 on 2016/2/18.
 */
public interface ISerializer<T> {
    String serialize(T t);

    T deserialize(String s);
}
