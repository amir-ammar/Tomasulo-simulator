package DataBusPackage;

public interface DataBusSubject {

    public void registerObserver(DataBusObserver o);

    public void removeObserver(DataBusObserver o);

    public void notifyObservers();

}
