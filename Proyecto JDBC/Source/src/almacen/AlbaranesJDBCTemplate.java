package almacen;

import java.util.List;

import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import almacen.Albaranes;

public class AlbaranesJDBCTemplate implements AlbaranesDAO{
	private DataSource ds;
	private JdbcTemplate template;
	
	@Override
	public void setDataSource(DataSource ds) {
		this.ds = ds;
		this.template = new JdbcTemplate(this.ds);
	}
	
	@Override
	public void actualizarAlbaran(int albaran, int cliente, String fecha_albaran, String fecha_envio, String fecha_pago, String formpago, String estado) {
		String sql = "UPDATE albaranes SET cliente=?, fecha_albaran=?, fecha_envio=?, fecha_pago=?, formpago=?, estado=? WHERE albaran=?";
		template.update(sql, cliente, fecha_albaran, fecha_envio, fecha_pago, formpago, estado, albaran);
		System.out.println("Registro actualizado del ALBARAN = " + albaran);
	}
	
	@Override
	public Albaranes selectAlbaran(int albaran) {
		String sql = "SELECT * FROM albaranes WHERE albaran = ?";
		Albaranes al = template.queryForObject(sql, new Object[]{albaran}, new AlbaranesMapper());
		
		return al;
	}
	
	@Override
	public void borrarAlbaran(int albaran) {
		String sql = "DELETE FROM albaranes WHERE albaran=?";
		template.update(sql, albaran);
		System.out.println("Registro eliminado: ALBARAN = " + albaran);
		
	}
	
	@Override
	public void nuevoAlbaran(int cliente, String fecha_albaran, String fecha_envio, String fecha_pago, String formpago, String estado) {
		String sql = "INSERT INTO albaranes (albaran, cliente, fecha_albaran, fecha_envio, fecha_pago, formpago, estado) VALUES ((SELECT MAX(albaran)+1 FROM(SELECT * FROM albaranes) as X),?,?,?,?,?,?)";
		template.update(sql, cliente, fecha_albaran, fecha_envio, fecha_pago, formpago, estado);
		System.out.println("Nuevo registro realizado");
	}
	
	@Override
	public List<Albaranes> selectAllAlbaranes() {
		List<Albaranes> al;
		
		String sql = "SELECT * FROM albaranes";
		
		al = template.query(sql, new AlbaranesMapper());
		
		return al;
	}

	@Override
	public void nuevaSQL(String sql) {
		template.update(sql);		
	}
	
}
