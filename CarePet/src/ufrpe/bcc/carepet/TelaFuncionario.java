package ufrpe.bcc.carepet;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.LayoutStyle.ComponentPlacement;

public class TelaFuncionario extends JFrame implements ActionListener{

	private JPanel contentPane;
	private JTextField txtCpfCliente;
	private JTextField txtNomeProduto;
	private JTextField txtNomeOuCpf;
	private JButton btnPesquisarProduto;
	private JButton btnConfirmarCliente;
	private JButton btnPesquisarCliente;
	private JLabel lblFuncpos;
	private Funcionario func;
	private JButton btnVoltar;
	private JButton btnCadastrarAgendamento;
	
	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaFuncionario frame = new TelaFuncionario();
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
	
	
	
	public TelaFuncionario(Funcionario func) {
		this.func = func;
		setTitle("CarePet - Seja Bem-Vindo!");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		btnConfirmarCliente = new JButton("Confirmar Cliente");
		
		btnPesquisarProduto = new JButton("Pesquisar Produto");
		
		txtCpfCliente = new JTextField();
		txtCpfCliente.setText("CPF Cliente");
		txtCpfCliente.setColumns(10);
		
		txtNomeProduto = new JTextField();
		txtNomeProduto.setText("Nome Produto");
		txtNomeProduto.setColumns(10);
		
		btnPesquisarCliente = new JButton("Pesquisar Cliente");
		btnConfirmarCliente.addActionListener(this);
		btnPesquisarProduto.addActionListener(this);
		btnPesquisarCliente.addActionListener(this);
		
		txtNomeOuCpf = new JTextField();
		txtNomeOuCpf.setText("Nome ou CPF");
		txtNomeOuCpf.setColumns(10);
			
				
				JLabel lblLogado = new JLabel("Logado como: "+func.getNome());
		
		lblFuncpos = new JLabel(func.getTipofunc());
		
		btnVoltar = new JButton("Voltar");
		btnVoltar.addActionListener(this);
		
		btnCadastrarAgendamento = new JButton("Cadastrar Agendamento");
		btnCadastrarAgendamento.addActionListener(this);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(39)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
											.addComponent(btnPesquisarCliente, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
											.addComponent(btnPesquisarProduto, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
											.addComponent(btnConfirmarCliente, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
										.addGroup(gl_contentPane.createSequentialGroup()
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(btnCadastrarAgendamento)))
									.addGap(46)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
										.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
											.addComponent(txtCpfCliente, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
											.addComponent(txtNomeProduto, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
											.addComponent(txtNomeOuCpf, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
										.addComponent(lblFuncpos)))
								.addComponent(lblLogado)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(btnVoltar)))
					.addContainerGap(147, Short.MAX_VALUE))
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
						.addComponent(btnPesquisarProduto)
						.addComponent(txtNomeProduto, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnPesquisarCliente)
						.addComponent(txtNomeOuCpf, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
					.addComponent(btnVoltar)
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);

	}
	
	public void actionPerformed(ActionEvent evento){
		if(evento.getSource().equals(btnVoltar)){
			dispose();
			TelaLogin tela = new TelaLogin();
			tela.setVisible(true);
		}
		if(evento.getSource().equals(btnCadastrarAgendamento)){
			dispose();
			TelaAgendamento tela = new TelaAgendamento(func);
			tela.setVisible(true);
		}
	}
}
