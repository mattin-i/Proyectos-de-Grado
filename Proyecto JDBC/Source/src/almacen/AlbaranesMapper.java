package almacen;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import almacen.Albaranes;

public class AlbaranesMapper implements RowMapper<Albaranes>{

	@Override
	public Albaranes mapRow(ResultSet rs, int arg1) throws SQLException {
		Albaranes al = new Albaranes();
		al.setAlbaran(rs.getInt("albaran"));
		al.setCliente(rs.getInt("cliente"));
		al.setFecha_albaran(rs.getString("fecha_albaran"));
		al.setFecha_envio(rs.getString("fecha_envio"));
		al.setFecha_pago(rs.getString("fecha_pago"));
		al.setFormpago(rs.getString("formpago"));
		al.setEstado(rs.getString("estado"));
		
		return al;
	}
}
