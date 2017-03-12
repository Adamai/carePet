package ufrpe.bcc.carepet;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
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

public class TelaCadastro extends JFrame implements ActionListener{

	private JPanel contentPane;
	private JTextField txtNome;
	private JTextField txtCpf;
	private JTextField txtEmail;
	private JTextField txtDdmmaaaa;
	private JTextField txtCidade;
	private JTextField txtCep;
	private JTextField txtLogradouro;
	private JButton btnCadastrar;
	private JButton btnVoltar;
	private JPasswordField txtSenha;
	
	/**
	 * Launch the application.
	 */
/*	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaCadastro frame = new TelaCadastro();
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
			"jdbc:mysql://localhost:3306/carepet?autoReconnect=true&useSSL=false", "root", "paloma"); // nome do esquema, usuário e senha
	return retorno;
}
	
	public TelaCadastro() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblNomeCompleto = new JLabel("Nome Completo:");
		
		txtNome = new JTextField();
		txtNome.setText("nome");
		txtNome.setColumns(10);
		
		JLabel lblCpflogin = new JLabel("CPF(login):");
		
		txtCpf = new JTextField();
		txtCpf.setText("cpf");
		txtCpf.setColumns(10);
		
		JLabel lblEmail = new JLabel("Email:");
		
		txtEmail = new JTextField();
		txtEmail.setText("email");
		txtEmail.setColumns(10);
		
		JLabel lblDataDeNascimento = new JLabel("Data de nascimento:");
		
		txtDdmmaaaa = new JTextField();
		txtDdmmaaaa.setText("dd/mm/aaaa");
		txtDdmmaaaa.setColumns(10);
		
		JLabel lblCidade = new JLabel("Cidade:");
		
		txtCidade = new JTextField();
		txtCidade.setText("cidade");
		txtCidade.setColumns(10);
		
		JLabel lblCep = new JLabel("CEP:");
		
		txtCep = new JTextField();
		txtCep.setText("apenas numeros");
		txtCep.setColumns(10);
		
		JLabel lblLogradouro = new JLabel("Logradouro:");
		
		txtLogradouro = new JTextField();
		txtLogradouro.setText("logradouro");
		txtLogradouro.setColumns(10);
		
		JLabel lblSenha = new JLabel("Senha:");
		
		btnCadastrar = new JButton("Cadastrar");
		
		btnVoltar = new JButton("Voltar");
		btnVoltar.addActionListener(this);
		btnCadastrar.addActionListener(this);
		
		txtSenha = new JPasswordField();
		txtSenha.setText("senha");
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addComponent(lblNomeCompleto)
										.addComponent(lblCpflogin)
										.addComponent(lblEmail))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addComponent(txtEmail, GroupLayout.PREFERRED_SIZE, 149, GroupLayout.PREFERRED_SIZE)
										.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
											.addComponent(txtNome, GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE)
											.addComponent(txtCpf, GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE))))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(lblDataDeNascimento)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(txtDdmmaaaa, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(lblCidade)
									.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addComponent(txtCep, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(txtCidade, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE)
										.addGroup(gl_contentPane.createSequentialGroup()
											.addGap(22)
											.addComponent(txtLogradouro, GroupLayout.PREFERRED_SIZE, 341, GroupLayout.PREFERRED_SIZE)))
									.addPreferredGap(ComponentPlacement.RELATED)))
							.addGap(182))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblCep)
							.addContainerGap(441, Short.MAX_VALUE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblLogradouro)
							.addContainerGap(405, Short.MAX_VALUE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblSenha)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(266)
									.addComponent(btnCadastrar))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(txtSenha, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)))
							.addContainerGap(83, Short.MAX_VALUE))))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(btnVoltar)
					.addContainerGap(535, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNomeCompleto)
						.addComponent(txtNome, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCpflogin)
						.addComponent(txtCpf, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(lblEmail)
						.addComponent(txtEmail, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDataDeNascimento)
						.addComponent(txtDdmmaaaa, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCidade)
						.addComponent(txtCidade, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(lblCep)
						.addComponent(txtCep, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblLogradouro)
						.addComponent(txtLogradouro, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblSenha)
								.addComponent(txtSenha, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED, 104, Short.MAX_VALUE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnCadastrar)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnVoltar))
		);
		contentPane.setLayout(gl_contentPane);
	}
	
	public void actionPerformed(ActionEvent evento){
		if(evento.getSource().equals(btnVoltar)){
			dispose();
			TelaLogin tela = new TelaLogin();
			tela.setVisible(true);
		}
		if(evento.getSource().equals(btnCadastrar)){
			try{
				Connection conec = getConexao();
				Statement cadastrarST = conec.createStatement();
				cadastrarST.execute(
						"INSERT INTO CLIENTE(CPF,email,dt_nasc,cidade,nome,CEP,Logradouro,senha) VALUES ('"
						+txtCpf.getText()+"','"+txtEmail.getText()+"',STR_TO_DATE('"+txtDdmmaaaa.getText()+"', '%d/%m/%Y'),'"+txtCidade.getText()+"','"
						+txtNome.getText()+"','"+txtCep.getText()+"','"+txtLogradouro.getText()+"','"+String.valueOf(txtSenha.getPassword())+"');"
						);
				JOptionPane.showMessageDialog(null, "Usuario Cadastrado\nFavor fazer login");
				dispose();
				cadastrarST.close();
				conec.close();
				TelaLogin tela = new TelaLogin();
				tela.setVisible(true);
				
			} catch (SQLException e) {
				System.out.println("Houve erro");
				e.printStackTrace();
			}
		}
	}
}
