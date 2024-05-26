import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class RegistrationServiceImpl extends UnicastRemoteObject implements RegistrationService {
    private List<Participant> participants;

    public RegistrationServiceImpl() throws RemoteException {
        participants = new ArrayList<>();
    }

    @Override
    public int registerParticipant(Participant participant) throws RemoteException {
        participants.add(participant);
        return participants.size();
    }

    @Override
    public List<Participant> getRegisteredParticipants() throws RemoteException {
        return participants;
    }
}