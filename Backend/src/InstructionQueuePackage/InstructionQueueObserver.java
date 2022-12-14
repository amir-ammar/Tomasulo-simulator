package InstructionQueuePackage;

import MessagesPackage.Message;

public interface InstructionQueueObserver {
    void instructionQueueUpdate(Message message);

}
