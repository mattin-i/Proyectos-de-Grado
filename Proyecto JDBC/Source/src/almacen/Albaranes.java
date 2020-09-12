package almacen;

public class Albaranes {
	private int albaran;
	private int cliente;
	private String fecha_albaran;
	private String fecha_envio;
	private String fecha_pago;
	private String formpago;
	private String estado;
	
	public int getAlbaran() {
		return albaran;
	}
	public int getCliente() {
		return cliente;
	}
	public String getFecha_albaran() {
		return fecha_albaran;
	}
	public String getFecha_envio() {
		return fecha_envio;
	}
	public String getFecha_pago() {
		return fecha_pago;
	}
	public String getFormpago() {
		return formpago;
	}
	public String getEstado() {
		return estado;
	}
	public void setAlbaran(int albaran) {
		this.albaran = albaran;
	}
	public void setCliente(int cliente) {
		this.cliente = cliente;
	}
	public void setFecha_albaran(String fecha_albaran) {
		this.fecha_albaran = fecha_albaran;
	}
	public void setFecha_envio(String fecha_envio) {
		this.fecha_envio = fecha_envio;
	}
	public void setFecha_pago(String fecha_pago) {
		this.fecha_pago = fecha_pago;
	}
	public void setFormpago(String formpago) {
		this.formpago = formpago;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	@Override
	public String toString() {
		return "Albaranes [albaran=" + albaran + ", cliente=" + cliente + ", fecha_albaran=" + fecha_albaran
				+ ", fecha_envio=" + fecha_envio + ", fecha_pago=" + fecha_pago + ", formpago=" + formpago + ", estado="
				+ estado + "]";
	}
}
