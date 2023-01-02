package RegisterFilePackage;

import MessagesPackage.Message;

public interface RegisterFileSubject {
    public void register(RegisterFileObserver observer);
    public void unregister(RegisterFileObserver observer);
    public void notifyObservers(Message message, char type);

}
