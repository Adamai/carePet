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

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;

public class TelaFuncionario extends JFrame implements ActionListener{

	private JPanel contentPane;
	private JTextField txtCpfCliente;
	private JTextField txtProduto;
	private JTextField txtCPF;
	private JButton btnProduto;
	private JButton btnConfirmarCliente;
	private JButton btnPesquisarCliente;
	private JLabel lblFuncpos;
	private Funcionario func;
	private JButton btnVoltar;
	private JButton btnCadastrarAgendamento;
	private JButton btnListaClienteagendamentos;
	private JButton btnCadastrar;
	
static {
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			System.out.println("Problemas carregando o Driver do MySQL");
		}
	}
	
public static Connection getConexao(Funcionario func) throws SQLException {
	
	Connection retorno = null;
	retorno = DriverManager.getConnection(
			"jdbc:mysql://localhost:3306/carepet?autoReconnect=true&useSSL=false", func.getCPF(), func.getSenha()); // nome do esquema, usuário e senha
	return retorno;
}
	
	public TelaFuncionario(Funcionario func) {
		this.func = func;
		setTitle("CarePet - Seja Bem-Vindo!");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		btnConfirmarCliente = new JButton("Confirmar Cliente");
		
		btnProduto = new JButton("Pesquisar Produto");
		btnProduto.addActionListener(this);
		
		txtCpfCliente = new JTextField();
		txtCpfCliente.setText("CPF Cliente");
		txtCpfCliente.setColumns(10);
		
		txtProduto = new JTextField();
		txtProduto.setText("Cod. do Produto");
		txtProduto.setColumns(10);
		
		btnPesquisarCliente = new JButton("Pesquisar Cliente");
		btnConfirmarCliente.addActionListener(this);
		btnProduto.addActionListener(this);
		btnPesquisarCliente.addActionListener(this);
		
		txtCPF = new JTextField();
		txtCPF.setText("CPF");
		txtCPF.setColumns(10);
			
				
				JLabel lblLogado = new JLabel("Logado como: "+func.getNome());
		
		lblFuncpos = new JLabel(func.getTipofunc());
		
		btnVoltar = new JButton("Voltar");
		btnVoltar.addActionListener(this);
		
		btnCadastrarAgendamento = new JButton("Cadastrar Agendamento");
		btnCadastrarAgendamento.addActionListener(this);
		
		btnListaClienteagendamentos = new JButton("Lista Cliente-Agendamentos");
		btnListaClienteagendamentos.addActionListener(this);
		
		btnCadastrar = new JButton("Cadastrar Cliente");
		btnCadastrar.addActionListener(this);
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(btnVoltar))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(39)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
										.addComponent(btnCadastrarAgendamento, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(btnPesquisarCliente, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(btnProduto, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(btnConfirmarCliente, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(btnListaClienteagendamentos, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(btnCadastrar, GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
										.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
											.addComponent(txtCpfCliente, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
											.addComponent(txtProduto, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
											.addComponent(txtCPF, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
										.addComponent(lblFuncpos)))
								.addComponent(lblLogado))))
					.addGap(86))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(lblLogado)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(lblFuncpos)
						.addComponent(btnCadastrarAgendamento))
					.addGap(9)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnConfirmarCliente)
						.addComponent(txtCpfCliente, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnProduto)
						.addComponent(txtProduto, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnPesquisarCliente)
						.addComponent(txtCPF, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnListaClienteagendamentos)
					.addGap(18)
					.addComponent(btnCadastrar)
					.addPreferredGap(ComponentPlacement.RELATED, 86, Short.MAX_VALUE)
					.addComponent(btnVoltar)
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);

	}
	
	public void actionPerformed(ActionEvent evento){
		if(evento.getSource().equals(btnCadastrar)){
			dispose();
			TelaCadastro tela = new TelaCadastro(func);
			tela.setVisible(true);
		}
		if(evento.getSource().equals(btnListaClienteagendamentos)){
			if(func.getTipofunc().equals("Tecnico")){
				dispose();
				TelaListaAgendamento tela = new TelaListaAgendamento(func);
				tela.setVisible(true);
				}
				else
					JOptionPane.showMessageDialog(null, "É necessário ser um Funcionario Técnico");
			
		}
		if(evento.getSource().equals(btnPesquisarCliente)){
			
		}
		if(evento.getSource().equals(btnConfirmarCliente)){
			if(func.getTipofunc().equals("Tecnico")){
			try{
				Connection conex = getConexao(func);
				PreparedStatement psGetcli = conex.prepareStatement("UPDATE cliente SET confirmadoc='1' WHERE cpf='"+txtCpfCliente.getText()+"';");
				psGetcli.execute();
				//if(psGetcli.execute("SELECT * FROM cliente WHERE cpf='"+txtCpfCliente.getText()+"';"))
				//	JOptionPane.showMessageDialog(null, "Cliente com CPF="+txtCpfCliente.getText()+" confirmado");
				//else
				JOptionPane.showMessageDialog(null, "Cliente com CPF="+txtCpfCliente.getText()+" confirmado");
				psGetcli.close(); conex.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			} else
				JOptionPane.showMessageDialog(null, "É necessário ser um Funcionario Técnico");
		
		}
		if(evento.getSource().equals(btnVoltar)){
			dispose();
			TelaLogin tela = new TelaLogin();
			tela.setVisible(true);
		}
		if(evento.getSource().equals(btnCadastrarAgendamento)){
			if(func.getTipofunc().equals("Tecnico")){
			dispose();
			TelaAgendamento tela = new TelaAgendamento(func);
			tela.setVisible(true);
			}
			else
				JOptionPane.showMessageDialog(null, "É necessário ser um Funcionario Técnico");
		}
		if(evento.getSource().equals(btnProduto)){
			try{
				Connection conex = getConexao(func);
				String query1 = "SELECT * FROM produto_ref WHERE cod="+txtProduto.getText()+" LIMIT 1;";
				PreparedStatement  psGetpro = conex.prepareStatement(query1);
				ResultSet rsGetpro = psGetpro.executeQuery();
				rsGetpro.next();
				String query2 = "SELECT * FROM instancia WHERE seq='"+rsGetpro.getString("seq_inst")+"' LIMIT 1;";
				PreparedStatement psGetinst = conex.prepareStatement(query2);
				ResultSet rsGetinst = psGetinst.executeQuery();
				rsGetinst.next();
				String query3 = "SELECT * FROM almoxarifado WHERE id="+rsGetinst.getString("id_almox")+" LIMIT 1;";
				PreparedStatement psGetalmo = conex.prepareStatement(query3);
				ResultSet rsGetalmo = psGetalmo.executeQuery();
				rsGetalmo.next();
				JOptionPane.showMessageDialog(null, "Quantidade: "+rsGetinst.getString("qtd_atual")+"\n"
						+ rsGetalmo.getString("descr"));
				rsGetalmo.close();psGetalmo.close();
				rsGetinst.close();psGetinst.close();
				rsGetpro.close();psGetpro.close();
				conex.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
