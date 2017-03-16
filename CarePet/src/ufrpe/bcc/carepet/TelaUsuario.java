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
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JScrollPane;
import javax.swing.JList;

public class TelaUsuario extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JButton btnCadastrarAnimal;
	private JButton btnVoltar;
	private Cliente usuario;
	private JButton btnCadastrarAgendamento;
	private DefaultListModel model = new DefaultListModel();
	private JList list = new JList();
	private JButton btnRem;
	private int ilista=0;
	
	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaUsuario frame = new TelaUsuario();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/
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
	/**
	 * Create the frame.
	 */
	public TelaUsuario(Cliente user) {
		usuario = user;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		
		try{
			Connection conex = getConexao();
			String query = "SELECT * FROM animal WHERE cpf_cliente = "+user.getCPF()+";";
			PreparedStatement psGetAni = conex.prepareStatement(query);
			ResultSet rsGetAni = psGetAni.executeQuery();
			while(rsGetAni.next()){
				model.addElement(rsGetAni.getString("nomea"));
				ilista++;
			}
			psGetAni.close();
			conex.close();
		} catch (SQLException e) {
			System.out.println("Houve erro");
			e.printStackTrace();
		}
		if(ilista!=0)
			list.setModel(model);
		
		btnCadastrarAnimal = new JButton("Cadastrar Animal");
		btnCadastrarAnimal.addActionListener(this);
		
		btnVoltar = new JButton("Voltar");
		btnVoltar.addActionListener(this);
		
		btnCadastrarAgendamento = new JButton("Cadastrar Agendamento");
		
		JScrollPane scrollPane = new JScrollPane();
		
		btnRem = new JButton("Remover Animal");
		btnRem.addActionListener(this);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(btnVoltar)
					.addContainerGap(413, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(btnRem))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(26)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(btnCadastrarAgendamento)
								.addComponent(btnCadastrarAnimal))
							.addPreferredGap(ComponentPlacement.RELATED, 66, Short.MAX_VALUE)
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 210, GroupLayout.PREFERRED_SIZE)))
					.addGap(21))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(48)
					.addComponent(btnCadastrarAnimal)
					.addGap(18)
					.addComponent(btnCadastrarAgendamento)
					.addPreferredGap(ComponentPlacement.RELATED, 417, Short.MAX_VALUE)
					.addComponent(btnVoltar))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(57)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 206, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnRem)
					.addContainerGap(260, Short.MAX_VALUE))
		);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane.setViewportView(scrollPane_1);
		
		
		scrollPane_1.setViewportView(list);
		contentPane.setLayout(gl_contentPane);
	}
	public void actionPerformed(ActionEvent evento){
		if(evento.getSource().equals(btnRem)){
			if(ilista!=0 && list.getSelectedValue()!=null){
				try{
					Connection conex = getConexao();
					String queryb = "SELECT * FROM animal WHERE cpf_cliente = "+usuario.getCPF()+" AND "
							+ "nomea='"+list.getSelectedValue().toString()+"' LIMIT 1;";
					String query = "DELETE FROM animal WHERE cpf_cliente = "+usuario.getCPF()+" AND "
							+ "nomea='"+list.getSelectedValue().toString()+"';";
					PreparedStatement psDelAni = conex.prepareStatement(queryb);
					ResultSet rsAni = psDelAni.executeQuery();
					rsAni.next();
					String idani = rsAni.getString("id");
					psDelAni.execute("DELETE FROM alergico WHERE id_animal = "+ "'"+idani+"';");
					psDelAni = conex.prepareStatement(query);
					psDelAni.execute();
					psDelAni.close();
					conex.close();
				} catch (SQLException e) {
					System.out.println("Houve erro");
					e.printStackTrace();
				}
				model.removeElement(list.getSelectedValue().toString());
				list.setModel(model);
				ilista--;
			}
		}
		if(evento.getSource().equals(btnVoltar)){
			dispose();
			TelaLogin tela = new TelaLogin();
			tela.setVisible(true);
		}
		if(evento.getSource().equals(btnCadastrarAnimal)){
			dispose();
			TelaCadastroAnimal tela = new TelaCadastroAnimal(usuario);
			tela.setVisible(true);
		}
		/*if(evento.getSource().equals(btnCadastrarAgendamento)){
			dispose();
			TelaAgendamento tela = new TelaAgendamento(usuario);
			tela.setVisible(true);
		}*/
	}
}
