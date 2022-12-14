package MainPackage;

import MainPackage.ExecutorObserver;

public interface ExecutorSubject {
    public void registerObserver(ExecutorObserver o);
    public void removeObserver(ExecutorObserver o);
    public void notifyObservers();
}
