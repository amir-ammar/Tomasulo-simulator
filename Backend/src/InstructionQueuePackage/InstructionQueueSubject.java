package InstructionQueuePackage;

import InstructionQueuePackage.InstructionQueueObserver;

public interface InstructionQueueSubject {
    public void registerObserver(InstructionQueueObserver o);
    public void removeObserver(InstructionQueueObserver o);
    public void notifyReservationStation();

    public void notifyLoadBuffer();

    public void notifyStoreBuffer();

}
