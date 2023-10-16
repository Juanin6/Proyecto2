package consola;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.invoke.StringConcatFactory;
import java.util.ArrayList;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import logica.AdministradorGeneral;
import logica.AdministradorLocal;
import logica.Cliente;
import logica.Empleado;
import logica.Reserva;
import logica.Sede;
import logica.CategoriaVehiculo;
import logica.Cliente;
import logica.ControladorReserva;
import logica.AdministradorLocal;
import logica.Seguro;

import logica.UsuarioGenerico;
import logica.Vehiculo;

public class EmpresaAlquilerVehiculos {
  private ControladorReserva controllerEmpresa = new ControladorReserva();
  private ArrayList<UsuarioGenerico> listaUsuarioGenericos;
  private ArrayList<Cliente> listaClientes ;
  private ArrayList<Sede> listaSedes;
  private ArrayList<Empleado> listaEmpleados;
  private ArrayList<CategoriaVehiculo> categoriaVehiculo;
  private AdministradorGeneral administradorGeneral;
  private ArrayList<Vehiculo> listaVehiculo ;
  private ArrayList<AdministradorLocal> listaAdministradorLocal;
  private ArrayList<Reserva> reservas = new ArrayList<Reserva>();
  private ArrayList<Seguro> seguros = new ArrayList<Seguro>();
  public static Integer numeroReservaInteger = 0 ;
  
  
  private void ejecutarPrograma() throws ParseException {
	 //Pruebas
	 UsuarioGenerico usuarioGenerico = new UsuarioGenerico("1", "1", "cliente");
	 listaUsuarioGenericos.add(usuarioGenerico);
	 Seguro seguro = new Seguro("a",1);
	 Seguro seguro1 = new Seguro("b",121);
	 Seguro seguro2 = new Seguro("c",141);
	 seguros.add(seguro);
	 seguros.add(seguro1);
	 seguros.add(seguro2);
	 //Pruebas
	 System.out.println("Bienvenido a la empresa");
	 String usuario=input("Usuario");
	 String contrasenia=input("Contraseña");
	 String tipoUsuario =login(usuario, contrasenia);
	 
	 if(tipoUsuario.equals("")) {
		 System.out.println("Usuario o contraseña incorrecta");
		 while (tipoUsuario.equals("")) {
			 System.out.println("Ingrese nuevamente su usuario y contraseña");
			 usuario=input("Usuario");
			 contrasenia=input("Contraseña");
			 tipoUsuario =login(usuario, contrasenia);
		 }
		 
		
	 } else {
		System.out.println("Entrando al sistema.....");
		
		if(tipoUsuario.equalsIgnoreCase("Cliente")){ 
			Cliente clienteLogin = null;
			for (Cliente cliente : listaClientes) {
				if(cliente.getUsuario().equals(usuario)&& cliente.getContraseña().equals(contrasenia)) 
					clienteLogin = cliente;
					break;
			}
			programaCliente(clienteLogin);
			
		}else if(tipoUsuario.equalsIgnoreCase("Empleado")) {
			Empleado empleadoLogin = null;
			for (Empleado empleado : listaEmpleados) {
				if(empleado.getUsuario().equals(usuario)&& empleado.getContraseña().equals(contrasenia)) 
					empleadoLogin = empleado;
					break;
			}programaEmpleado(empleadoLogin);
			
		} else if (tipoUsuario.equalsIgnoreCase("Administrador General")) {
			programaAdministradorGeneral(administradorGeneral);
		}
		
		else if(tipoUsuario.equalsIgnoreCase("Administrador Local")) {
			AdministradorLocal administradorLocalLogin = null;
			for (AdministradorLocal AdmiL : listaAdministradorLocal) {
				if(AdmiL.getUsuario().equals(usuario)&& AdmiL.getContraseña().equals(contrasenia)) 
					administradorLocalLogin = AdmiL;
					break;
			}programaAdministradorLocal(administradorLocalLogin);
		}
		
	}
	 
 }

private String login(String usuario,String contrasenia) {
	 
	 for (UsuarioGenerico usuarioGenerico : listaUsuarioGenericos) {
		String userFor = usuarioGenerico.getUsuario();
		String passwordFor = usuarioGenerico.getContraseña();
		if(usuario.equals(userFor) && passwordFor.equals(contrasenia)) {
			String tipoUsario= usuarioGenerico.getTipoUsuario();
			return tipoUsario;
		}
		
	}return "";
 }
 
 private void programaCliente (Cliente clienteLogin) throws ParseException {
	 MenuCliente();
	 int option = Integer.parseInt(input("Ingrese la opcion que desea"));
	 
	 if(option == 1) {
		 ArrayList<Integer> segurosPosiciones = new ArrayList<Integer>();
		 String categoria=input("Ingrese el tipo de vehiculo que desea ");
		 String nombreSede = input("Ingrese la sede en la que desea recogerlo");
		 String fechaI= input("Ingrese la fecha de inicio formato: yyyy-MM-dd HH:mm");
		 String fechaF= input("Ingrese la fecha de finalizacion formato: yyyy-MM-dd HH:mm");
		 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		 Date fechaInicio =format.parse(fechaI);
		 Date fechaFinal = format.parse(fechaF);
		 try {
		Vehiculo vehiculo = controllerEmpresa.ReservaVehiculo(categoria, categoriaVehiculo, nombreSede, fechaInicio, fechaFinal, listaSedes);
		String sedeDevolver = input("Ingrese la sede que desea devolverlo");
		mostrarSeguros();
		int seguro =Integer.parseInt(input("Ingrese el numero del seguro que desea agregar"));
		boolean conSeguro = false;
		if(seguro!=0) {
			segurosPosiciones.add(seguro);
			String masSeguro = input("Desea agregar otro seguro Si(1) , No(0)");
			while(masSeguro.equals("1")) {
				seguro = Integer.parseInt(input("Ingrese el numero del seguro que desea agregar"))  ;
				segurosPosiciones.add(seguro);
				masSeguro = input("Desea agregar otro seguro Si(1) , No(0)");
			}
			conSeguro = true;
		}
		
		
		
		
		double valorSinSeguro= controllerEmpresa.ValorReservaSinSeguro(vehiculo,listaSedes,sedeDevolver);
		reservas.add(controllerEmpresa.CrearReservaCliente(clienteLogin,valorSinSeguro,administradorGeneral,conSeguro, vehiculo,nombreSede,sedeDevolver,seguros,segurosPosiciones));
		numeroReservaInteger+=1;
		System.out.println("Creando la reserva... ");
		Thread.sleep(100);
		System.out.println("Su valor a pagar es"+reservas.get(reservas.size()-1).getPrecio30());
		System.out.println("Se hizo el cobro a la tarjeta del 30% del valor de la reserva");
		System.out.println("Cuando recoga el vehiculo se hara el cobro restante");
		
		 } catch (ParseException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		 
	 } 
	 else if(option==2) {
		 ArrayList<Integer> segurosPosiciones = new ArrayList<Integer>();
		 Empleado empleadoSede = null;
		 String categoria=input("Ingrese el tipo de vehiculo que desea ");
		 String nombreSede = input("Ingrese la sede en la que se encuentra");
		 String fechaF= input("Ingrese la fecha de inicio formato: yyyy-MM-dd HH:mm"); 
		 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		 Date fechaInicio = new Date();
		 Date fechaFinal = format.parse(fechaF);
		 Vehiculo vehiculo = controllerEmpresa.ReservaVehiculo(categoria, categoriaVehiculo, nombreSede, fechaInicio, fechaFinal, listaSedes);
		 String sedeDevolver = input("Ingrese la sede que desea devolverlo");
		 mostrarSeguros();
			int seguro =Integer.parseInt(input("Ingrese el numero del seguro que desea agregar"));
			boolean conSeguro = false;
			if(seguro!=0) {
				segurosPosiciones.add(seguro);
				String masSeguro = input("Desea agregar otro seguro Si(1) , No(0)");
				while(masSeguro.equals("1")) {
					seguro = Integer.parseInt(input("Ingrese el numero del seguro que desea agregar"))  ;
					segurosPosiciones.add(seguro);
					masSeguro = input("Desea agregar otro seguro Si(1) , No(0)");
				}
				conSeguro = true;
			}
		 double valorSinSeguro= controllerEmpresa.ValorReservaSinSeguro(vehiculo,listaSedes,sedeDevolver);
		 Reserva alquiler;
		 alquiler =(controllerEmpresa.CrearReservaCliente(clienteLogin,valorSinSeguro,administradorGeneral,conSeguro, vehiculo,nombreSede,sedeDevolver,seguros,segurosPosiciones));
		 numeroReservaInteger+=1;
		 reservas.add(alquiler);
		 
		 for (Sede sedess: listaSedes) {
			 if(sedess.getNombre().equalsIgnoreCase(nombreSede)) {
				empleadoSede= sedess.getEmpleados().get(0);
				break;
				 }	 
			
			 } 
		 String conductorAdicional = input("Desea agregar otro conductor Si(1) No(0)");
		 boolean aditional = conductorAdicional.equals("1");
		 double valorConductorExtra=0;
		 if(aditional) {
			 SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			 int numero  = Integer.parseInt(input("Ingrese el numero de su liciencia "));
			 String paisExpedicion = input("Ingrese el pais de expedicion de la licencia");
			 Date  fechaVencimiento =  dateFormat.parse(input("Ingrese la fecha de vencimiento de su licencia en formato: dd/MM/yyyy")); 
			Double valorTotalDouble = alquiler.getVehiculo().getCategoria().getTarifario().getValorExtra2Conduc();
			valorConductorExtra = empleadoSede.agregarConductor(valorTotalDouble,alquiler,numero,paisExpedicion,fechaVencimiento);
			 }
		 empleadoSede.administarRecogidaCliente(alquiler);
		 
		 System.out.println("Su valor a pagar es"+(reservas.get(reservas.size()-1).getPrecioTotal()+valorConductorExtra));
		 System.err.println("Sus datos fueron validados ");
		 System.err.println("Pago exitoso ");
		 System.out.println("Las llaves estan encima del vidrio");
	 }
		 
	 }
 private void MenuCliente() {
	 System.out.println("1.Reservar vehiculo");
	 System.out.println("2 Alquilar vehiculo");
	 
 }
 private void menuEmpleado() {
	 System.out.println("1.Recoger vehiculo cliente ");
	 System.out.println("2.Devolucion del vehiculo cliente");
	 
 }
 private void menuAdministradorGeneral (){ 

	 System.out.println("1 Registrar compra de un nuevo vehiculo");
	 System.out.println("2 Dar de baja un vehiculo");
 }
 
 private void mostrarSeguros() {
	 int i =0;
	 System.out.println("0 No agregar ningun seguro ");
	 for (Seguro seguro : seguros) {
		i+=1;
		System.out.println( i + seguro.getnombre()+":"+seguro.getPrecio());
	}
 }

 private void programaAdministradorGeneral (AdministradorGeneral administradorGeneral) {
	menuAdministradorGeneral();
	int option = Integer.parseInt(input("Ingrese la opcion que desea"));
	if(option==1) {
		modificarVehiculoAdministradorGeneral();
	}else if(option==2) {
		darDeBajaVehiculoAdmin();
	}
	
 }
 private void darDeBajaVehiculoAdmin(){
	 
	 int idVehiculo =Integer.parseInt(input("Id del vehiculo"));
	 Vehiculo vehiculoDeBajaVehiculo=null;
	 
	 for (Vehiculo vehiculo : listaVehiculo) {
		if(idVehiculo==(vehiculo.getIdVehiculo())) {
			vehiculoDeBajaVehiculo = vehiculo;
		}
	}
	administradorGeneral.darDeBajaVehiculo(listaVehiculo, vehiculoDeBajaVehiculo, vehiculoDeBajaVehiculo.getSedeActual());
	System.out.println("Vehiculo dado de baja");
 }
 
 
 private void modificarVehiculoAdministradorGeneral() {
	 
	 	String nombreSedeString = input("Ingrese el nombre de la sede donde se registrara el coche");
		String modelo =input("Ingrese el modelo del vehiculo");
		int capacidad = Integer.parseInt( input("Ingrese la capacidad del vehiculo"));
		String placa = input("Ingrese la placa del carro");
		String color = input("Ingrese el color del vehiculo");
		String tipoTransmision = input("Tipo de transmision");
		String categoriaVehiculo = input("Categoria del vehiculo");
		
		Sede sede = controllerEmpresa.buscarSedePorNombre(nombreSedeString, listaSedes);
		CategoriaVehiculo categoria =null;
		
		for (CategoriaVehiculo categoriaVehiculo2 : this.categoriaVehiculo) {
			if(categoriaVehiculo.equals(categoriaVehiculo2.getNombreCategoria())) {
				categoria = categoriaVehiculo2;
			}
		}
		Vehiculo vehiculo = new Vehiculo(0, false, sede,nombreSedeString, capacidad, placa,
				                         modelo, color, tipoTransmision, categoria, null,
				                         null, false, false);
		
		administradorGeneral.registrarCompraVehiculo(vehiculo, sede,listaVehiculo);
		System.out.println("Vehiculo registrado en el sistema");
 }

 
 private void programaEmpleado(Empleado empleadoLogin) throws ParseException {
	 menuEmpleado();
	 Reserva reservaClienteInterno=null;
	 int option = Integer.parseInt(input("Ingrese la opcion que desea"));
	 if(option == 1) {
		 Integer numeroReserva = Integer.parseInt(input("Ingrese el identificador de reserva del cliente"));
		for (Reserva reserva : reservas) {
			if(numeroReserva.equals(reserva.getIdentificador()));{
			reservaClienteInterno = reserva;}
		}
		 double valorAdicional = 0;
		 String adicional = input("El cliente desea agregar otro conductor Si(1) No (0)");
		 if(adicional.equals("1")) {
			int numero = Integer.parseInt(input("Numero de licencia"));
			String pais = input("Pais donde fue radicada");
			valorAdicional = reservaClienteInterno.getVehiculo().getCategoria().getTarifario().getValorExtra2Conduc();
			SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
			Date fechaVencimiento = formatoFecha.parse(input("Fecha de vencimiento de la licencia yyyy-MM-dd"));
			
			valorAdicional= empleadoLogin.agregarConductor(valorAdicional, reservaClienteInterno, numero, pais, fechaVencimiento);
			
		 }
		 double tarifaTotal = reservaClienteInterno.getPrecioRestante() + valorAdicional;
		 empleadoLogin.administarRecogidaCliente(reservaClienteInterno);
		 System.out.println("Verificando los datos del cliente");
		 System.out.println("La tarifa total a pagar del cliente son"+ tarifaTotal);
		 System.out.println("Generando cobro a la tarjeta.....");
		 System.out.println("Pago exitoso");
	 }else if(option==2) {
		 Reserva reservaClienteLogin=null;
		 Integer numeroReserva = Integer.parseInt(input("Ingrese el identificador de reserva del cliente"));
		 for (Reserva reservasCliente : reservas) {
				if(numeroReserva.equals(reservasCliente.getIdentificador())) 
					reservaClienteLogin = reservasCliente; 
	 }   empleadoLogin.devolucionCocheCliente(reservaClienteLogin);
	 System.out.println("El carro fue devuelto con exito");
	 }
 } 

 
 private void programaAdministradorLocal(AdministradorLocal administradorLocalLogin) {
	// TODO Auto-generated method stub
	
}
 
 
 
 
 
 public static void main(String[] args) throws ParseException {
	 
	 EmpresaAlquilerVehiculos programa = new EmpresaAlquilerVehiculos();
	 ControllerCarga control = new ControllerCarga();
	 programa.cargaDatos(control);
	 programa.ejecutarPrograma();
}
 
 private Cliente buscarClienteSistema(String nombreCliente,ArrayList<Cliente> clientes) {
	 Cliente clienteEncontrado;
	 for (Cliente cliente2 : clientes) {
		if(cliente2.getNombre().equalsIgnoreCase(nombreCliente)) {
			clienteEncontrado = cliente2;
			return clienteEncontrado;
		}
	} return null;
 }
 private void cargaDatos(ControllerCarga control) {
	  
	 listaClientes = control.cargarClientes("./data/clientes.txt\\");
	 listaEmpleados = control.cargarEmpleados("./data/empleados.txt\\");
	 categoriaVehiculo = control.cargarCategoria("./data/tarifas.txt\\\\");
	 listaVehiculo = control.cargarVehiculos(categoriaVehiculo,"./data/vehiculos.txt\\");
	 listaAdministradorLocal = control.cargarAdministradorLocal("./data/administradorLocal.txt\\");
	 administradorGeneral = control.cargarAdministradorGeneral("./data/administradorGeneral.txt\\");
	 listaSedes = control.cargarSedes(listaEmpleados, listaVehiculo, listaAdministradorLocal, "./data/sedes.txt\\");
	 listaUsuarioGenericos = control.cargaUsuarios(listaEmpleados, listaClientes, listaAdministradorLocal, administradorGeneral);
     }
 
 
 
 
 
 
 public static Integer getNumeroReservaInteger() {
	return numeroReservaInteger;
 }
public String input(String mensaje)
	{
		try
		{
			System.out.print(mensaje + ": ");
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			return reader.readLine();
		}
		catch (IOException e)
		{
			System.out.println("Error leyendo de la consola");
			e.printStackTrace();
		}
		return null;
	}
}
