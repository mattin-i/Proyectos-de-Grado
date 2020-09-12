package almacen;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import almacen.Albaranes;
import almacen.AlbaranesJDBCTemplate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class AlbaranesTest {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int res;
		
		// Conexión con el XML a la base de datos
		ApplicationContext context = new ClassPathXmlApplicationContext("springjdbc.xml");
		AlbaranesJDBCTemplate al = (AlbaranesJDBCTemplate) context.getBean("AlbaranesJDBCTemplate");
		
		// Menu del programa
		do {
			System.out.println("MENU");
			System.out.println("1. Nuevo registro");
			System.out.println("2. Actualizar registro");
			System.out.println("3. Borrar registro");
			System.out.println("4. Consultar registro");
			System.out.println("5. Rellenar XML");
			System.out.println("6. Recoger XML");
			System.out.println("7. Salir");
			System.out.print("---->");
			res = sc.nextInt();
			
			switch(res) {
				case 1:
					nuevoRegistro(al);
					
					break;
					
				case 2:
					actualizarRegistro(al);
					
					break;
					
				case 3:
					borrarRegistro(al);
					
					break;
					
				case 4:
					consultarRegistro(al);
					
					break;
				
				case 5:
					rellenarXML(al);
					
					break;
					
				case 6:
					leerXML(al);
										
					break;
					
				case 7:
					System.out.println("Cerrando...");
					
					break;
					
				default:
					break;
			}
			
		}while(res!=7);
		
		System.out.println("Programa finalizado");
	}
	
	public static void nuevoRegistro(AlbaranesJDBCTemplate al) {
		Scanner sc = new Scanner(System.in);
		int cliente;
		String fecha_albaran, fecha_envio, fecha_pago, formpago, estado;
		
		System.out.println("Introduce datos para nuevo registro:");
		
		System.out.println("Código cliente:");
		cliente = sc.nextInt();
		sc.nextLine();
		
		System.out.println("Fecha albaran (AAAA-MM-DD):");
		fecha_albaran = sc.nextLine();
		
		System.out.println("Fecha albaran (AAAA-MM-DD):");
		fecha_envio = sc.nextLine();
		
		System.out.println("Fecha albaran (AAAA-MM-DD):");
		fecha_pago = sc.nextLine();
		
		System.out.println("Forma de pago (C/T)");
		formpago = sc.nextLine();
		
		System.out.println("Estado (S/N)");
		estado = sc.nextLine();
		
		al.nuevoAlbaran(cliente, fecha_albaran, fecha_envio, fecha_pago, formpago, estado);
	}
	
	public static void actualizarRegistro(AlbaranesJDBCTemplate al) {
		Scanner sc = new Scanner(System.in);
		int albaran, cliente;
		String fecha_albaran, fecha_envio, fecha_pago, formpago, estado;
		
		System.out.println("Introduce datos para actualizar registro:");
		
		System.out.println("Código albaran para actualizar:");
		albaran = sc.nextInt();
		
		Albaranes nAlActu = al.selectAlbaran(albaran);
		System.out.println(nAlActu.toString());
		
		System.out.println("");
		System.out.println("Introduce los nuevos datos:");
		
		System.out.println("Código cliente:");
		cliente = sc.nextInt();
		sc.nextLine();
		
		System.out.println("Fecha albaran (AAAA-MM-DD):");
		fecha_albaran = sc.nextLine();
		
		System.out.println("Fecha albaran (AAAA-MM-DD):");
		fecha_envio = sc.nextLine();
		
		System.out.println("Fecha albaran (AAAA-MM-DD):");
		fecha_pago = sc.nextLine();
		
		System.out.println("Forma de pago (C/T)");
		formpago = sc.nextLine();
		
		System.out.println("Estado (S/N)");
		estado = sc.nextLine();
		
		al.actualizarAlbaran(albaran, cliente, fecha_albaran, fecha_envio, fecha_pago, formpago, estado);
	}
	
	public static void consultarRegistro(AlbaranesJDBCTemplate al) {
		Scanner sc = new Scanner(System.in);
		int albaran;
		
		System.out.println("Código de albaran para consulta:");
		albaran = sc.nextInt();
		
		Albaranes nAlb = al.selectAlbaran(albaran);
		System.out.println(nAlb.toString());
	}
	
	public static void borrarRegistro(AlbaranesJDBCTemplate al) {
		Scanner sc = new Scanner(System.in);
		int albaran;
		char confir;
		
		System.out.println("Código de albaran para borrar:");
		albaran = sc.nextInt();
		
		Albaranes nAlBor = al.selectAlbaran(albaran);
		System.out.println(nAlBor.toString());
		
		do {
			System.out.println("Seguro que quieres borrar este registro? (S/s / N/n)");
			confir = sc.next().charAt(0);
		} while (confir != 's' && confir != 'n' && confir != 'S' && confir != 'N');
		
		
		if (confir == 's' || confir == 'S') {
			al.borrarAlbaran(albaran);
		} else {
			System.out.println("No se ha borrado ningún registro.");
		}
	}
	
	public static void rellenarXML(AlbaranesJDBCTemplate al) {
		String nombre_archivo = "albaranes";
		ArrayList<String> albaranXML = new ArrayList<String>();
		ArrayList<String> clienteXML = new ArrayList<String>();
		ArrayList<String> fechaAlbaranXML = new ArrayList<String>();
		ArrayList<String> fechaEnvioXML = new ArrayList<String>();
		ArrayList<String> fechaPagoXML = new ArrayList<String>();
		ArrayList<String> formpagoXML = new ArrayList<String>();
		ArrayList<String> estadoXML = new ArrayList<String>();
		
		List<Albaranes> listAlb = al.selectAllAlbaranes();
		
		for (int i = 0; i < listAlb.size(); i++) {
			albaranXML.add("" + listAlb.get(i).getAlbaran());
			clienteXML.add("" + listAlb.get(i).getCliente());
			fechaAlbaranXML.add(listAlb.get(i).getFecha_albaran());
			fechaEnvioXML.add(listAlb.get(i).getFecha_envio());
			fechaPagoXML.add(listAlb.get(i).getFecha_pago());
			formpagoXML.add(listAlb.get(i).getFormpago());
			estadoXML.add(listAlb.get(i).getEstado());
		}
		
		try {
			generateXML(nombre_archivo, albaranXML, clienteXML, fechaAlbaranXML, fechaEnvioXML, fechaPagoXML, formpagoXML, estadoXML);
			System.out.println("XML de albaranes generado");
		} catch (Exception e) {}
	}
	
	public static void generateXML(String name, ArrayList<String> albaran, ArrayList<String> cliente, ArrayList<String> fechaAlbaran, ArrayList<String> fechaEnvio, ArrayList<String> fechaPago, ArrayList<String> formpago, ArrayList<String> estado) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        DOMImplementation implementation = builder.getDOMImplementation();
        Document document = implementation.createDocument(null, name, null);
        document.setXmlVersion("1.0");
        
        Element root = document.getDocumentElement();
        
        for (int i = 0; i < albaran.size(); i++) {
        	Element firstNode = document.createElement("albaran");
        	
        	Element nodeAlb = document.createElement("albaran");
        	Text nodeAlbValue = document.createTextNode(albaran.get(i));
        	nodeAlb.appendChild(nodeAlbValue);
        	
        	Element nodeCli = document.createElement("cliente");
        	Text nodeCliValue = document.createTextNode(cliente.get(i));
        	nodeCli.appendChild(nodeCliValue);
        	
        	Element nodeFecAl = document.createElement("fecha_albaran");
        	Text nodeFecAlValue = document.createTextNode(fechaAlbaran.get(i));
        	nodeFecAl.appendChild(nodeFecAlValue);
        	
        	Element nodeFecEn = document.createElement("fecha_envio");
        	Text nodeFecEnValue = document.createTextNode(fechaEnvio.get(i));
        	nodeFecEn.appendChild(nodeFecEnValue);
        	
        	Element nodeFecPag = document.createElement("fecha_pago");
        	Text nodeFecPagValue = document.createTextNode(fechaPago.get(i));
        	nodeFecPag.appendChild(nodeFecPagValue);
        	
        	Element nodeFormp = document.createElement("formpago");
        	Text nodeFormpValue = document.createTextNode(formpago.get(i));
        	nodeFormp.appendChild(nodeFormpValue);
        	
        	Element nodeEst = document.createElement("estado");
        	Text nodeEstValue = document.createTextNode(estado.get(i));
        	nodeEst.appendChild(nodeEstValue);
        	
        	firstNode.appendChild(nodeAlb);
        	firstNode.appendChild(nodeCli);
        	firstNode.appendChild(nodeFecAl);
        	firstNode.appendChild(nodeFecEn);
        	firstNode.appendChild(nodeFecPag);
        	firstNode.appendChild(nodeFormp);
        	firstNode.appendChild(nodeEst);
        	
        	root.appendChild(firstNode);
        }
        
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File("C:/Users/adminportatil/workspace/AaD - Proyecto/src/listalbaranes.xml"));
        
        transformer.transform(source, result);
	}
	
	public static void leerXML(AlbaranesJDBCTemplate al) {
		String table_name = "PROYECTO_AaD_2DM3_GRUPO2";
		
		try {
			String sql="CREATE TABLE " + table_name + " (cliente int(3), empresa varchar(25), apellidos varchar(25), nombre varchar(25), direccion1 varchar(25), direccion2 varchar(25), poblacion varchar(25), provincia int(2), distrito int(5), telefono varchar(25), formpago varchar(25), total_factura decimal(11,2))";
			al.nuevaSQL(sql);
			System.out.println("Nueva tabla creada!");
			
			File fXmlFile = new File("C:/Users/adminportatil/workspace/AaD - Proyecto/src/listgrupo3.xml"); // Aqui iría la ruta del xml del siguiente grupo
			DocumentBuilderFactory lecFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder lecBuilder = lecFactory.newDocumentBuilder();
			Document lecDoc = lecBuilder.parse(fXmlFile);
			
			lecDoc.getDocumentElement().normalize();
			
			NodeList nList = lecDoc.getElementsByTagName("cliente");
			
			for (int i = 0; i < nList.getLength(); i++) {
				Node nNode = nList.item(i);
				
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element el = (Element) nNode;
					
					String elCli = el.getElementsByTagName("cliente").item(0).getTextContent() + ", '";
					String elEm = el.getElementsByTagName("empresa").item(0).getTextContent() + "', '";
					String elAp = el.getElementsByTagName("apellidos").item(0).getTextContent() + "', '";
					String elNom = el.getElementsByTagName("nombre").item(0).getTextContent() + "', '";
					String elDir1 = el.getElementsByTagName("direccion1").item(0).getTextContent() + "', '";
					String elDir2 = el.getElementsByTagName("direccion2").item(0).getTextContent() + "', '";
					String elPob = el.getElementsByTagName("poblacion").item(0).getTextContent() + "', ";
					String elPro = el.getElementsByTagName("provincia").item(0).getTextContent() + ", ";
					String elDis = el.getElementsByTagName("distrito").item(0).getTextContent() + ", '";
					String elTel = el.getElementsByTagName("telefono").item(0).getTextContent() + "', '";
					String elFor = el.getElementsByTagName("formpago").item(0).getTextContent() + "', ";
					String elTot = el.getElementsByTagName("total_factura").item(0).getTextContent();
					
					String sql2 = "INSERT INTO " + table_name + " VALUES (" + elCli + elEm + elAp + elNom + elDir1 + elDir2 + elPob + elPro + elDis + elTel + elFor + elTot + ")";
					
					al.nuevaSQL(sql2);
				}
			}
			System.out.println("Datos añadidos a la nueva tabla!");
			
		} catch(Exception e) {
			System.out.println("ERROR:" + e);
		}
	}
}