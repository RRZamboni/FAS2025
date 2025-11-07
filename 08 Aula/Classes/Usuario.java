
public class Usuario 
{
	//Atributos
	private String usuario;
	private String consulta;
	private String timestamp;
	
	//Método Construtor I
	public Usuario()
	{
		
	}

	//Método Construtor II
	public Usuario(String usuario, String consulta, String timestamp) 
	{
		super();
		this.usuario = usuario;
		this.consulta = consulta;
		this.timestamp = timestamp;
	}

	
	//Getters & Setters
	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getConsulta() {
		return consulta;
	}

	public void setConsulta(String consulta) {
		this.consulta = consulta;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	
	//Método ToString
	@Override
	public String toString() 
	{
		return "Usuario [usuario=" + usuario 
				+ ", consulta=" + consulta 
				+ ", timestamp=" + timestamp + "]";
	}
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	
}
