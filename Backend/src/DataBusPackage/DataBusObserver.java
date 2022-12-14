package DataBusPackage;

import MessagesPackage.Message;

public interface DataBusObserver {
    public void DataBusUpdate(Message message);
}
