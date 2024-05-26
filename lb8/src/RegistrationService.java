import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RegistrationService extends Remote {
    int registerParticipant(Participant participant) throws RemoteException;
    List<Participant> getRegisteredParticipants() throws RemoteException;
}