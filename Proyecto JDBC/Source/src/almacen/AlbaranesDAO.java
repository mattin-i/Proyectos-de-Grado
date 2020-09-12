package almacen;

import java.util.List;

import javax.sql.DataSource;

import almacen.Albaranes;

public interface AlbaranesDAO {
	public void setDataSource(DataSource ds);
	public void actualizarAlbaran(int albaran, int cliente, String fecha_albaran, String fecha_envio, String fecha_pago, String formpago, String estado);
	public Albaranes selectAlbaran(int albaran);
	public void borrarAlbaran(int albaran);
	public void nuevoAlbaran(int cliente, String fecha_albaran, String fecha_envio, String fecha_pago, String formpago, String estado);
	public List<Albaranes> selectAllAlbaranes();
	public void nuevaSQL(String sql);
}