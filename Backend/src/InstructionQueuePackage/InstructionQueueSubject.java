package InstructionQueuePackage;

import InstructionQueuePackage.InstructionQueueObserver;

public interface InstructionQueueSubject {
    public void registerObserver(InstructionQueueObserver o);
    public void removeObserver(InstructionQueueObserver o);
    public void notifyObservers();
}
