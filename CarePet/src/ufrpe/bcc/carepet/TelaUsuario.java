package ufrpe.bcc.carepet;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;

public class TelaUsuario extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JButton btnCadastrarAnimal;
	private JButton btnVoltar;
	private Cliente usuario;
	private JButton btnCadastrarAgendamento;
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

	/**
	 * Create the frame.
	 */
	public TelaUsuario(Cliente user) {
		usuario = user;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		btnCadastrarAnimal = new JButton("Cadastrar Animal");
		btnCadastrarAnimal.addActionListener(this);
		
		btnVoltar = new JButton("Voltar");
		btnVoltar.addActionListener(this);
		
		btnCadastrarAgendamento = new JButton("Cadastrar Agendamento");
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(btnVoltar)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(94)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(btnCadastrarAgendamento)
								.addComponent(btnCadastrarAnimal))))
					.addContainerGap(203, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(71)
					.addComponent(btnCadastrarAnimal)
					.addGap(18)
					.addComponent(btnCadastrarAgendamento)
					.addPreferredGap(ComponentPlacement.RELATED, 94, Short.MAX_VALUE)
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
