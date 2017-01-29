package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import javax.swing.*;
import common.IntServidorJuegoRMI;
import server.IntServidorPartidasRMI;

public class Juego {

	public static final int NUMFILAS = 8, NUMCOLUMNAS = 8, NUMBARCOS = 6;

	private GuiTablero guiTablero = null; 
	private IntServidorPartidasRMI partida = null; 
	private int quedan = NUMBARCOS, disparos = 0;
	
	public Juego(IntServidorJuegoRMI h){
		try {
			this.partida = (IntServidorPartidasRMI) h.nuevoServidorPartidas();
			partida.nuevaPartida(NUMFILAS, NUMCOLUMNAS, NUMBARCOS);
			this.guiTablero = new GuiTablero(NUMFILAS, NUMCOLUMNAS);
		} catch (RemoteException e) {
			System.out.println("Hubo un problema al establecer conexión");
			
		}
		
	}

	/******************************************************************************************/
	/********************* CLASE INTERNA GuiTablero ****************************************/
	/******************************************************************************************/
	public class GuiTablero {

		private int numFilas, numColumnas;

		private JFrame frame = null; // Tablero de juego
		private JLabel estado = null; // Texto en el panel de estado
		private JButton buttons[][] = null; // Botones asociados a las casillas
											// de la partida

		private JPanel panelPrincipal = null;

		/**
		 * Constructor de una tablero dadas sus dimensiones
		 */
		GuiTablero(int numFilas, int numColumnas) {
			this.numFilas=numFilas;
			this.numColumnas=numColumnas;
			dibujaTablero();//creo y añado el tablero
			anyadeMenu(); //creo y añado el menu
			anyadeGrid(numFilas, numColumnas); // creo y añado la matriz de botones
			anyadePanelEstado("Intentos: " + disparos + ", Barcos restantes: "
					+ quedan); //creo y añado la etiqueta que va informando sobre la partida
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //le digo, que al cerrar la app, se cierre la api de Java
			frame.setSize(300, 300);//pongo un tamaño del frame de 300
			frame.setResizable(false);
			frame.setVisible(true);

		}

		/**
		 * Dibuja el tablero de juego y crea la partida inicial
		 */
		private void dibujaTablero() {
			this.frame = new JFrame("Barcos"); //creo el frame
			this.panelPrincipal = new JPanel(); //creo un panel
			panelPrincipal.setLayout(new BorderLayout()); //le indico la horientacion de BorderLayout al panel
			frame.add(panelPrincipal); //añado el panel al frame
		} // end dibujaTablero

		/**
		 * Anyade el menu de opciones del juego y le asocia un escuchador
		 */
		private void anyadeMenu() {

			JPanel panel = new JPanel(new BorderLayout()); //creo un panel principal
			JMenuBar barra = new JMenuBar(); //creo un objeto barra
			JMenu menu = new JMenu("Opciones"); //y un contenedor de la barra que tenga las diferentes opciones
			barra.add(menu); 
			JMenuItem item1, item2, item3; //creo los 3 items albergados en la barra
			MenuListener mL = new MenuListener();
			item1 = new JMenuItem("Salir");
			item1.addActionListener(mL);
			item2 = new JMenuItem("Nueva partida");
			item2.addActionListener(mL);
			item3 = new JMenuItem("Muestra solución");
			item3.addActionListener(mL);
			menu.add(item1);
			menu.add(item2);
			menu.add(item3);
			panel.add(barra,BorderLayout.WEST); //lo añado todo al panel
			panelPrincipal.add(panel, BorderLayout.NORTH);

		} // end anyadeMenu

		/**
		 * Anyade el panel con las casillas del mar y sus etiquetas. Cada
		 * casilla sera un boton con su correspondiente escuchador
		 * 
		 * @param nf
		 *            numero de filas
		 * @param nc
		 *            numero de columnas
		 */
		private void anyadeGrid(int nf, int nc) {

			ButtonListener escuchador = new ButtonListener(); //creo un objeto que sirva de referencia para todos los botones
			buttons = new JButton[nf][nc]; //creo una tabla de botones
			JPanel panel = new JPanel(new GridLayout(nf+1, nc+2)); //creo una grid con el conjunto total de filas y columnas
			//Ahora relleno la fila superior con letras y espacios en blanco
			panel.add(new JLabel(""));
			for (int i =1; i<nc+1; i++)
				panel.add(new JLabel(String.valueOf(i),JLabel.CENTER));
			panel.add(new JLabel(""));
			char cont='A';
			for (int PosX = 0; PosX < nf; PosX++) {//aqui relleno con botones y en los lados con las referencias de labels
				panel.add(new JLabel(String.valueOf(cont),JLabel.CENTER));
				for (int PosY = 0; PosY < nc; PosY++) {
					buttons[PosX][PosY] = new JButton();
					buttons[PosX][PosY].putClientProperty("fila", PosX);
					buttons[PosX][PosY].putClientProperty("col", PosY);
					buttons[PosX][PosY].addActionListener(escuchador);
					panel.add(buttons[PosX][PosY]);
				}
				panel.add(new JLabel(String.valueOf(cont),JLabel.CENTER));
				cont++;
			}
			panelPrincipal.add(panel, BorderLayout.CENTER);//aquí ajusto el layout al centro
		} // end anyadeGrid

		/**
		 * Anyade el panel de estado al tablero
		 * 
		 * @param cadena
		 *            cadena inicial del panel de estado
		 */

		private void anyadePanelEstado(String cadena) {
			
			JPanel panel = new JPanel();
			estado = new JLabel();
			estado.setText(cadena);
			panel.add(estado);
			frame.add(panel, BorderLayout.SOUTH);

		} // end anyadePanel Estado

		/**
		 * Cambia la cadena mostrada en el panel de estado
		 * 
		 * @param cadenaEstado
		 *            nuevo estado
		 */
		private void cambiaEstado(String cadenaEstado) {
			estado.setText(cadenaEstado);
		} // end cambiaEstado
		
		
		/**
		 * Devuelve una cadena mostrando el estado actual
		 */
		public String getEstado(){
			return estado.getText();
		}

		/**
		 * Muestra la solucion de la partida y marca la partida como finalizada
		 */
		private void muestraSolucion() {
			cambiaEstado("La partida se acabó");//ponemos la label final, de partida acabada
			for (int i=0; i<numFilas; i++){//primero pintamos todo de azul
				for (int j=0; j<numColumnas;j++){
					pintaBoton(buttons[i][j], Color.CYAN);
				}
			}
			try {
				for (String s:partida.getsolucion())//ahora pintamos todos lo barcos, invocando a la fun pintabarcoHundido
					pintaBarcoHundido(s);
			} catch (RemoteException e) {
				System.out.println("hubo un problema con la conexión");
			}
			
			quedan=0;
		} // end muestraSolucion

		/**
		 * Pinta un barco como hundido en el tablero
		 * 
		 * @param cadenaBarco
		 *            cadena con los datos del barco codificados como
		 *            "filaInicial#columnaInicial#orientacion#tamanyo"
		 */
		private void pintaBarcoHundido(String cadenaBarco) {
			String[] datosBarcos = cadenaBarco.split("#");//meto en un vector los datos del barco
			//obtengo en cada variable los datos del barco, fila, col, cant y orientacion
			int filaIni = Integer.parseInt(datosBarcos[0]);
			int colIni = Integer.parseInt(datosBarcos[1]);
			int cant = Integer.parseInt(datosBarcos[3]);
			char orientacion = datosBarcos[2].charAt(0);
			if (orientacion == 'V') {//si en vertical coloreo las filas del barco
				int cont = 0;
				for (int i = filaIni; cont < cant; i++) {
					pintaBoton(buttons[i][colIni], Color.RED);
					cont++;
				}
			} else {//lo mismo pero en horizontal
				int cont = 0;
				for (int i = colIni; cont < cant; i++) {
					pintaBoton(buttons[filaIni][i], Color.RED);
					cont++;
				}

			}
		}

		/**
		 * Pinta un botón de un color dado
		 * 
		 * @param b
		 *            boton a pintar
		 * @param color
		 *            color a usar
		 */
		private void pintaBoton(JButton b, Color color) {
			//estos pasos son obligatorios para Ios , para cualquier otro SSOO también funcionaría correctamente,
			//pero sería suficiente con b.setBackground(color)
			b.setBackground(color);
			b.setOpaque(true);
			b.setBorderPainted(false);
		} // end pintaBoton

		/**
		 * Limpia las casillas del tablero pintándolas del gris por defecto
		 */
		private void limpiaTablero() {
			for (int i = 0; i < numFilas; i++) {
				for (int j = 0; j < numColumnas; j++) {
					pintaBoton(buttons[i][j], null);
				}
			}
		} // end limpiaTablero

		/**
		 * Destruye y libera la memoria de todos los componentes del frame
		 */
		private void liberaRecursos() {
			frame.dispose();//libero de la memoria todos los componentes del frame al salir

		} // end liberaRecursos
		
	} // end class GuiTablero

	/******************************************************************************************/
	/********************* CLASE INTERNA MenuListener ****************************************/
	/******************************************************************************************/

	/**
	 * Clase interna que escucha el menu de Opciones del tablero
	 * 
	 */
	private class MenuListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			JMenuItem opcion = (JMenuItem) e.getSource(); //obtengo el botón que ha sido tocado
			if (opcion.getText().equals("Salir")) {//si el boton es de salir-->salgo
				guiTablero.liberaRecursos();
				System.exit(0);
			}
			if (opcion.getText().equals("Nueva partida")) { //si el boton es de jugar una nueva partida
				guiTablero.limpiaTablero();
				guiTablero.limpiaTablero();
				try {
					partida.nuevaPartida(NUMFILAS, NUMCOLUMNAS, NUMBARCOS);
				} catch (RemoteException e1) {
					System.out.println("Hubo un problema con la conexión");
				}
				quedan = NUMBARCOS;
				disparos = 0;
				guiTablero.cambiaEstado("Intentos: " + disparos
								+ ", Barcos restantes: " + quedan);
				
			}
			if (opcion.getText().equals("Muestra solución")) {
				guiTablero.muestraSolucion(); //pinto el tablero con las soluciones
				guiTablero.muestraSolucion();
			}
		} // end class MenuListener
	}

	/******************************************************************************************/
	/********************* CLASE INTERNA ButtonListener **************************************/
	/******************************************************************************************/
	/**
	 * Clase interna que escucha cada uno de los botones del tablero Para poder
	 * identificar el boton que ha generado el evento se pueden usar las
	 * propiedades de los componentes, apoyandose en los metodos
	 * putClientProperty y getClientProperty
	 */
	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// POR IMPLEMENTAR
			if (quedan!=0) {
				//compruebo que aun quedan barcos, sino, no dejo iterar, 
				// para que no dejar seguir la partida, una vez se hayan hundido todos los barcos
				JButton boton = (JButton) e.getSource();
				disparos++; //aunmento el numero de disparos efectuados
				
				//ahora obtengo las coordenadas de los botones, para saber que boton ha sido pulsado
				int i = (Integer) boton.getClientProperty("fila");
				int j = (Integer) boton.getClientProperty("col");
				//ahora llamo a pruebacasilla para que gestione el disparo efectuado,
				//y obtengo el resultado que debo gestionar
				int resultado = 0;
				try {
					resultado = partida.pruebaCasilla(i, j);
				} catch (RemoteException e1) {
					System.out.println("hubo un problema con la conexión");
				}
				if (resultado >= 0) {//si me ha devuelto un nº positivo, tengo que hundir(pintar de rojo)
									//un barco
					try {
						guiTablero.pintaBarcoHundido(partida.getBarco(resultado));
					} catch (RemoteException e1) {
						System.out.println("Hubo un problema con la conexión");
					} 
					//ahora lo pinto y quito un barco del contador
					quedan--;
				
				} else { //pinto de azul o de naranja dependiendo de si es agua, o si solo he tocado una casilla de un barco
					if (resultado == -1) {
						guiTablero.pintaBoton(boton, Color.CYAN);
					}
					if (resultado == -2) {
						guiTablero.pintaBoton(boton, Color.ORANGE);
					}
				}
			
				if (quedan == 0) { //aqui se trata de poner el label de que se acabó, en caso de que ya no queden mas barcos no hundidos,
									//o bien cambiar el label con la informacion correspondiente
					guiTablero.cambiaEstado("La partida se acabó");
				
				} else {
					guiTablero.cambiaEstado("Intentos: " + disparos
						+ ", Barcos restantes: " + quedan);
				}
			}

		} // end actionPerformed

	} // end class ButtonListener

}// end class Juego
