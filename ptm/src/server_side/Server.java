package server_side;

public interface Server {
	public void start(ClientHandler c);
	public void stop();
}
