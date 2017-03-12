package ufrpe.bcc.carepet;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.JPasswordField;

public class TelaLogin extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JTextField fieldLogin;
	private JButton btnEntrar;
	private JButton btnCadastrar;
	private JPasswordField fieldSenha;

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaLogin frame = new TelaLogin();
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
				"jdbc:mysql://localhost:3306/carepet?autoReconnect=true&useSSL=false", "root", "kbrito"); // nome do esquema, usu�rio e senha
		return retorno;
	}
	
	public TelaLogin() {
		setTitle("CarePet");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblLogin = new JLabel("Login(CPF):");
		
		JLabel lblSenha = new JLabel("Senha:");
		
		fieldLogin = new JTextField();
		fieldLogin.setColumns(10);
		
		btnEntrar = new JButton("Entrar");
		btnEntrar.addActionListener(this);
		
		btnCadastrar = new JButton("Cadastar");
		btnCadastrar.addActionListener(this);
		
		fieldSenha = new JPasswordField();
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(139)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(lblLogin)
						.addComponent(lblSenha))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
						.addComponent(fieldLogin)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(14)
							.addComponent(btnEntrar))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(9)
							.addComponent(btnCadastrar))
						.addComponent(fieldSenha))
					.addContainerGap(133, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(93)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblLogin)
						.addComponent(fieldLogin, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(27)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblSenha)
						.addComponent(fieldSenha, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnEntrar)
					.addGap(18)
					.addComponent(btnCadastrar)
					.addContainerGap(16, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
		
		
		
	}
	public void actionPerformed(ActionEvent evento){
		if(evento.getSource().equals(btnEntrar)){
			try{
				String cpf = fieldLogin.getText();
				String senha = String.valueOf(fieldSenha.getPassword());
				Connection conex = getConexao();
				String query = "SELECT * FROM CLIENTE WHERE CPF ='"+ cpf +"' LIMIT 1";
				int userfunc = 1;
				PreparedStatement psCPF = conex.prepareStatement(query);
				ResultSet rsCPF = psCPF.executeQuery();
				if(!(rsCPF.first())){
					query = "SELECT * FROM FUNCIONARIO WHERE CPF ='"+ cpf +"' LIMIT 1";
					psCPF = conex.prepareStatement(query);
					rsCPF = psCPF.executeQuery();
					userfunc = 2;
				}
				if(!(rsCPF.first())){
					JOptionPane.showMessageDialog(null, "Usuario nao encontrado");
				}
				else{		//SUCESSO! USU�RIO ENCONTRADO!
					if(rsCPF.first() && senha.equals(rsCPF.getString("senha"))){
						if(userfunc==1){		//usuario logando
							Cliente user = new Cliente(rsCPF);
							dispose();
							TelaUsuario tela = new TelaUsuario(user);
							tela.setVisible(true);
							psCPF.close();
							conex.close();
						}
						else if(userfunc==2){		//funcionario logando
							Funcionario func = new Funcionario(rsCPF);
							dispose();
							TelaFuncionario tela = new TelaFuncionario(func);
							tela.setVisible(true);
							psCPF.close();
							conex.close();
						}
					}
					else{
						//System.out.println(rsCPF.getString("senha"));
						//System.out.println(senha);
						JOptionPane.showMessageDialog(null, "Senha Incorreta");
					}
				}
			} catch (SQLException e) {
				System.out.println("Houve erro");
				e.printStackTrace();
			}
		}
		if(evento.getSource().equals(btnCadastrar)){
			dispose();
			TelaCadastro tela = new TelaCadastro();
			tela.setVisible(true);
		}
	}
}
