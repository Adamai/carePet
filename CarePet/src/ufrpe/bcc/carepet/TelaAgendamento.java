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
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JTextArea;

public class TelaAgendamento extends JFrame implements ActionListener{

	private JPanel contentPane;
	private JTextField textDia;
	private JTextField textMes;
	private JTextField textAno;
	private JTextField textHora;
	private JTextField FieldCPF;
	private JButton btnCriarAgendamento;
	private JButton btnConsultarAnimais;
	private JTextField FieldCodServ;
	private JButton btnChecarDescr;
	private JTextArea txtrDescrip;
	private JLabel lblTipoServ;
	private ResultSet rsTipoServ;
	private JTextField textObs;
	private JTextField textPrice;
	private DefaultListModel model;
	private JButton btnVoltar;
	private Funcionario funcionario;
	private JTextField textHoraF;
	private JComboBox comboTipo;
	private JList list;
	private JLabel lblMotivo;
	private JTextField textCliMotivo;
	private JTextField textLabNome;
	private JTextField textLabTel;
	private JLabel lblTelefone;
	private JLabel lblNomeDoLa;

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaAgendamento frame = new TelaAgendamento();
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
	
	public TelaAgendamento(Funcionario func) {
		funcionario = func;
		setTitle("CarePet - Agendamento");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1100, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblLogadoComo = new JLabel("Logado como: "+func.getNome());
		
		String[] TiposAtend = {"CLI", "LAB", "PET"};
		comboTipo = new JComboBox(TiposAtend);

		
		comboTipo.setToolTipText("Tipo do Agendamento");
		
		JLabel lblTipoDeAgendamento = new JLabel("Tipo de Agendamento:");
		
		JLabel lblData = new JLabel("Data(dd/mm/aa):");
		
		textDia = new JTextField();
		textDia.setColumns(10);
		
		textMes = new JTextField();
		textMes.setColumns(10);
		
		textAno = new JTextField();
		textAno.setColumns(10);
		
		JLabel lblHorario = new JLabel("Horario:");
		
		textHora = new JTextField();
		textHora.setText("00:00");
		textHora.setColumns(10);
		
		FieldCPF = new JTextField();
		FieldCPF.setColumns(10);
		
		JLabel lblCpfCliente = new JLabel("CPF Cliente");
		model = new DefaultListModel();
		list = new JList(model);
		
		btnCriarAgendamento = new JButton("Criar Agendamento");
		
		btnConsultarAnimais = new JButton("Consultar Animais");
		
		btnCriarAgendamento.addActionListener(this);
		btnConsultarAnimais.addActionListener(this);
		
		lblTipoServ = new JLabel("Tipo de Servi\u00E7o:");
		
		JLabel lblCodServ = new JLabel("Cod. de Servi\u00E7o:");
		
		FieldCodServ = new JTextField();
		FieldCodServ.setColumns(10);
		
		txtrDescrip = new JTextArea();
		txtrDescrip.setText("Descri\u00E7\u00E3o");
		
		btnChecarDescr = new JButton("Checar Descr.");
		btnChecarDescr.addActionListener(this);
		
		JLabel lblObs = new JLabel("Obs:");
		
		textObs = new JTextField();
		textObs.setColumns(10);
		
		JLabel lblPrice = new JLabel("Pre\u00E7o:");
		
		textPrice = new JTextField();
		textPrice.setText("000.00");
		textPrice.setColumns(10);
		
		btnVoltar = new JButton("Voltar");
		btnVoltar.addActionListener(this);
		
		JLabel lblHorarioF = new JLabel("Hor\u00E1rio previsto para t\u00E9rmino:");
		
		textHoraF = new JTextField();
		textHoraF.setText("00:00");
		textHoraF.setColumns(10);
		
		lblMotivo = new JLabel("Motivo (Apenas para atendimento CLI)");
		
		textCliMotivo = new JTextField();
		textCliMotivo.setColumns(10);
		
		textLabNome = new JTextField();
		textLabNome.setColumns(10);
		
		textLabTel = new JTextField();
		textLabTel.setColumns(10);
		
		lblTelefone = new JLabel("Telefone:");
		
		lblNomeDoLa = new JLabel("Nome do Laborat\u00F3rio (LAB)");
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblLogadoComo))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(btnVoltar)
					.addContainerGap(1013, Short.MAX_VALUE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(39)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblMotivo, GroupLayout.DEFAULT_SIZE, 504, Short.MAX_VALUE)
							.addGap(45)
							.addComponent(lblNomeDoLa)
							.addGap(440))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(textPrice, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblPrice)
							.addContainerGap())
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblObs)
							.addContainerGap())
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
								.addComponent(textObs)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
											.addGroup(gl_contentPane.createSequentialGroup()
												.addComponent(lblTipoDeAgendamento)
												.addGap(34)
												.addComponent(comboTipo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
											.addGroup(gl_contentPane.createSequentialGroup()
												.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
													.addComponent(lblData)
													.addComponent(lblHorario))
												.addPreferredGap(ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
												.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
													.addGroup(gl_contentPane.createSequentialGroup()
														.addComponent(textDia, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
														.addPreferredGap(ComponentPlacement.RELATED)
														.addComponent(textMes, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
														.addPreferredGap(ComponentPlacement.RELATED)
														.addComponent(textAno, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE))
													.addComponent(textHora, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
										.addComponent(lblTipoServ)
										.addGroup(gl_contentPane.createSequentialGroup()
											.addComponent(lblCodServ)
											.addGap(18)
											.addComponent(FieldCodServ, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_contentPane.createSequentialGroup()
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(btnChecarDescr))
										.addGroup(gl_contentPane.createSequentialGroup()
											.addGap(33)
											.addComponent(lblHorarioF)
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(textHoraF, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
								.addComponent(txtrDescrip, GroupLayout.DEFAULT_SIZE, 641, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.RELATED, 249, Short.MAX_VALUE)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(lblCpfCliente)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(FieldCPF, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
										.addComponent(btnCriarAgendamento, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(list, GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE))
									.addContainerGap())
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(10)
									.addComponent(btnConsultarAnimais, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addContainerGap())))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(textCliMotivo, GroupLayout.PREFERRED_SIZE, 307, GroupLayout.PREFERRED_SIZE)
							.addGap(112)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(textLabNome, GroupLayout.PREFERRED_SIZE, 239, GroupLayout.PREFERRED_SIZE)
								.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
									.addComponent(lblTelefone)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(textLabTel)))
							.addGap(377))))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblLogadoComo)
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(comboTipo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblTipoDeAgendamento)
						.addComponent(FieldCPF, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblCpfCliente))
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(18)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblData)
								.addComponent(textAno, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(textMes, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(textDia, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblHorario)
								.addComponent(textHora, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblHorarioF)
								.addComponent(textHoraF, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addComponent(lblTipoServ)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(FieldCodServ, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblCodServ)
								.addComponent(btnChecarDescr)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(6)
							.addComponent(btnConsultarAnimais)
							.addGap(13)
							.addComponent(list, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)))
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(15)
							.addComponent(btnCriarAgendamento))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(txtrDescrip, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblObs)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textObs, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblPrice)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textPrice, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(33)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblMotivo)
						.addComponent(lblNomeDoLa))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(textCliMotivo, GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnVoltar))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(textLabNome, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(textLabTel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblTelefone))
							.addContainerGap())))
		);
		contentPane.setLayout(gl_contentPane);
	}
	
	public void actionPerformed(ActionEvent evento){
		if(evento.getSource().equals(btnChecarDescr)){
			try{
				Connection conex = getConexao();
				String query = "SELECT * FROM tipo_servico WHERE cod = '"+FieldCodServ.getText()+"' LIMIT 1";
				PreparedStatement psTipoServ = conex.prepareStatement(query);
				rsTipoServ = psTipoServ.executeQuery();
				if(rsTipoServ.first()){
					String descri = rsTipoServ.getString("descr");
					txtrDescrip.setText(rsTipoServ.getString("descr"));
					lblTipoServ.setText(rsTipoServ.getString("categoria"));
					psTipoServ.close();
					conex.close();
				} else{
					txtrDescrip.setText("Serviço não encontrado");
					psTipoServ.close();
					conex.close();
				}
				
			} catch (SQLException e) {
				System.out.println("Houve erro");
				e.printStackTrace();
			}
		}
		if(evento.getSource().equals(btnConsultarAnimais)){
			try{
				Connection conex = getConexao();
				String query = "SELECT * FROM animal WHERE cpf_cliente = "+FieldCPF.getText()+";";
				PreparedStatement psGetAni = conex.prepareStatement(query);
				ResultSet rsGetAni = psGetAni.executeQuery();
				while(rsGetAni.next()){
					model.addElement(rsGetAni.getString("nomea"));
				}
				
			} catch (SQLException e) {
				System.out.println("Houve erro");
				e.printStackTrace();
			}
		}
		if(evento.getSource().equals(btnVoltar)){
			dispose();
			TelaFuncionario tela = new TelaFuncionario(funcionario);
			tela.setVisible(true);
		}
		if(evento.getSource().equals(btnCriarAgendamento)){
			try{
				Connection conec = getConexao();
				Statement cadastrarST = conec.createStatement();
				cadastrarST.execute("INSERT INTO fatura(stats, vl_total, dt_venc) VALUES("
						+ "'aguardando confirmação de pagamento',"+textPrice.getText()+",DATE_ADD(now() , INTERVAL 7 DAY));");
				ResultSet rscodfat = cadastrarST.executeQuery("SELECT * FROM fatura where stats='aguardando confirmação de pagamento'"
						+ " AND vl_total="+textPrice.getText()+" ORDER BY 'cod' DESC LIMIT 1;");		
				rscodfat.next();
				cadastrarST.execute("INSERT INTO agendamento(tipo_agendamento,hora_inicio,hora_fim,dt_agenda,cod_fatura) VALUES('"
						+ comboTipo.getSelectedItem()+"',STR_TO_DATE('"+textHora.getText()+"', '%k:%i'),STR_TO_DATE('"+
						textHoraF.getText()+"', '%k:%i'),now(),"+rscodfat.getString("cod")+");"); //ERRO NO COD WTF
						
				cadastrarST.execute("INSERT INTO realiza(cpf_cliente,cpf_func,data_marcada) VALUES('"+FieldCPF.getText()+"','"+
						funcionario.getCPF()+"',NOW());"						
						);
				
				Statement busca = conec.createStatement();
				ResultSet rbusca = busca.executeQuery("SELECT * FROM agendamento ORDER BY 'id' DESC LIMIT 1;");
				rbusca.next();
				String idagen = rbusca.getString("id");
				rbusca = busca.executeQuery("SELECT * FROM animal WHERE nomea='"+list.getSelectedValue().toString()+
						"' and cpf_cliente="+FieldCPF.getText()+";");
				rbusca.next();
				String idani = rbusca.getString("id");
				//pegar idagend do agendamento feito e usar para inserir em o envolve
				
				cadastrarST.execute("INSERT INTO envolve(id_animal, id_agend) VALUES("+idani+","+idagen+");");
				if(comboTipo.equals("CLI")){
					
				} else if (comboTipo.equals("PET")){
					
				} else{//LAB
					
				}
				rscodfat.close();
				rbusca.close();
				cadastrarST.close();
				busca.close();
				conec.close();
			}catch (SQLException e) {
				System.out.println("Houve erro no cadastro do agendamento");
				e.printStackTrace();
			}
			JOptionPane.showMessageDialog(null, "Agendamento Realizado");
			dispose();
			TelaFuncionario tela = new TelaFuncionario(funcionario);
			tela.setVisible(true);
		}
	}
}
