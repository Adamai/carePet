package ufrpe.bcc.carepet;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class TelaCadastroAnimal extends JFrame implements ActionListener{

	private JPanel contentPane;
	private JTextField txtNome;
	private JTextField txtDdmmaaaa;
	private JList listRaca;
	private JButton btnVerObs;
	private JLabel lblObs;
	private ResultSet racaRS;
	private JLabel lblPorte;
	private JButton btnVoltar;
	private JButton btnCadastrar;
	private Cliente usuario;
	
	
	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaCadastroAnimal frame = new TelaCadastroAnimal();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the frame.
	 */
	
static {
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			System.out.println("Problemas carregando o Driver do MySQL");
		}
	}
	
	public static Connection getConexao() throws SQLException {
		
		Connection retorno = null;
		retorno = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/carepet?autoReconnect=true&useSSL=false", "root", "kbrito"); // nome do esquema, usuário e senha
		return retorno;
	}
	
	public TelaCadastroAnimal(Cliente user) {
		usuario = user;
		setTitle("Cadastro Animal");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblNome = new JLabel("Nome:");
		
		txtNome = new JTextField();
		txtNome.setText("nome");
		txtNome.setColumns(10);
		
		JLabel lblDataDeNascimento = new JLabel("Data de nascimento:");
		
		txtDdmmaaaa = new JTextField();
		txtDdmmaaaa.setText("dd/mm/aaaa");
		txtDdmmaaaa.setColumns(10);
		
		JLabel lblcasoNoSaiba = new JLabel("(caso n\u00E3o saiba, pode dar data aproximada)");
		
		
		
		ArrayList racas = new ArrayList();
		try{
		Connection conec = getConexao();
		String query = "SELECT * FROM RAÇA";
		PreparedStatement getRacasST = conec.prepareStatement(query);
		racaRS = getRacasST.executeQuery();
		while(racaRS.next()){
			racas.add(racaRS.getString("descr"));
		}
		getRacasST.close();
		racaRS.close();
		conec.close();
		}catch (SQLException e) {
			System.out.println("Houve erro");
			e.printStackTrace();
		}
		
		JLabel lblRaca = new JLabel("Descri\u00E7\u00E3o de Ra\u00E7a:");
		
		btnVerObs = new JButton("Ver observa\u00E7\u00E3o");
		btnVerObs.addActionListener(this);
		
		
		JLabel lblDescrio = new JLabel("Observa\u00E7\u00E3o:");
		
		JScrollPane scrollPane = new JScrollPane();
		
		lblObs = new JLabel("");
		
		lblPorte = new JLabel("Porte");
		
		btnCadastrar = new JButton("Cadastrar");
		
		btnVoltar = new JButton("Voltar");
		btnCadastrar.addActionListener(this);
		btnVoltar.addActionListener(this);
	
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(lblNome)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(txtNome, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(lblDataDeNascimento)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(txtDdmmaaaa, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(33)
							.addComponent(lblcasoNoSaiba))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 277, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnVerObs))
						.addComponent(lblDescrio)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblRaca))
						.addComponent(lblObs)
						.addComponent(lblPorte))
					.addContainerGap(74, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addContainerGap(255, Short.MAX_VALUE)
					.addComponent(btnVoltar)
					.addGap(39)
					.addComponent(btnCadastrar)
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblNome)
								.addComponent(txtNome, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblDataDeNascimento)
								.addComponent(txtDdmmaaaa, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblcasoNoSaiba)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblRaca)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 154, GroupLayout.PREFERRED_SIZE))
						.addComponent(btnVerObs))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblDescrio)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblObs)
							.addPreferredGap(ComponentPlacement.RELATED, 102, Short.MAX_VALUE)
							.addComponent(lblPorte)
							.addGap(35))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnCadastrar)
								.addComponent(btnVoltar))
							.addContainerGap())))
		);
		listRaca = new JList(racas.toArray());
		scrollPane.setViewportView(listRaca);
		contentPane.setLayout(gl_contentPane);
	}
	public void actionPerformed(ActionEvent evento){
		if(evento.getSource().equals(btnVerObs)){
			try{
				Connection conec = getConexao();
				String query = "SELECT * FROM raça WHERE descr='"+listRaca.getSelectedValue().toString()+"' LIMIT 1;";
				//System.out.println(listRaca.getSelectedValue().toString());
				PreparedStatement getDescr = conec.prepareStatement(query);
				ResultSet descrRS = getDescr.executeQuery();
				descrRS.next();
				lblObs.setText(descrRS.getString("obs"));
				lblPorte.setText(descrRS.getString("porte_animal"));
				
				getDescr.close();
				descrRS.close();
				conec.close();
				}catch (SQLException e) {
					System.out.println("Houve erro");
					e.printStackTrace();
				}
		}
		
		if(evento.getSource().equals(btnVoltar)){
			dispose();
			TelaUsuario tela = new TelaUsuario(usuario);
			tela.setVisible(true);
		}
		if(evento.getSource().equals(btnCadastrar)){
			try{
				Connection conec = getConexao();
				Statement cadastrarST = conec.createStatement();
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
				LocalDate localDate = LocalDate.now();		//dtf.format(localDate) == 2016/11/16
				String hoje = dtf.format(localDate).toString();
				int idade;
				int mesnasc = Integer.parseInt(txtDdmmaaaa.getText().substring(3, 5));
				int mesatu = Integer.parseInt(hoje.substring(5, 7));
				int anonasc = Integer.parseInt(txtDdmmaaaa.getText().substring(6));
				int anoatu = Integer.parseInt(hoje.substring(0,4));
				int datanasc = Integer.parseInt(txtDdmmaaaa.getText().substring(0, 2));
				int dataatu = Integer.parseInt(hoje.substring(8));
				idade = anoatu - anonasc;
				if(mesatu>=mesnasc){
					if(mesatu==mesnasc&&dataatu<datanasc)
						idade = idade-1;
				} else{
					idade = idade -1;
				}
				//System.out.println(hoje);
				String query = "SELECT * FROM raça WHERE descr='"+listRaca.getSelectedValue().toString()+"' LIMIT 1;";
				//System.out.println(listRaca.getSelectedValue().toString());
				PreparedStatement getDescr = conec.prepareStatement(query);
				ResultSet descrRS = getDescr.executeQuery();
				descrRS.next();
				cadastrarST.execute("INSERT INTO animal(nomea,dt_nasc,idade,cpf_cliente,cod_raça) VALUES ('"
						+txtNome.getText()+"',STR_TO_DATE('"+txtDdmmaaaa.getText()+"', '%d/%m/%Y'),"+idade+",'"+usuario.getCPF()+
						"',"+descrRS.getString("cod")+");");
				
				
			}catch (SQLException e) {
				System.out.println("Houve erro no cadastro do animal");
				e.printStackTrace();
			}
			JOptionPane.showMessageDialog(null, "Animal Cadastrado");
			dispose();
			TelaUsuario tela = new TelaUsuario(usuario);
			tela.setVisible(true);
		}
	}
}
