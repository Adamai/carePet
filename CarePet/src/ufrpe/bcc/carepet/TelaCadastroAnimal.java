package ufrpe.bcc.carepet;

import java.awt.BorderLayout;
import java.awt.Component;
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
import javax.swing.DefaultListModel;
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
	private ResultSet substRS;
	private JLabel lblPorte;
	private JButton btnVoltar;
	private JButton btnCadastrar;
	private Cliente usuario;
	private JButton btnAddtolist;
	protected JList listAler;
	protected JList listSub;
	protected DefaultListModel listaSubs = new DefaultListModel();
	protected DefaultListModel listaAler = new DefaultListModel();
	private JButton btnRemList;
	
	
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
				"jdbc:mysql://localhost:3306/carepet?autoReconnect=true&useSSL=false", "root", "paloma"); // nome do esquema, usuário e senha
		return retorno;
	}
	
	public TelaCadastroAnimal(Cliente user) {
		usuario = user;
		setTitle("Cadastro Animal");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 630);
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
		
		ArrayList subst = new ArrayList();
		try{
		Connection conec = getConexao();
		String query = "SELECT * FROM SUBSTANCIA";
		PreparedStatement getSubstST = conec.prepareStatement(query);
		substRS = getSubstST.executeQuery();
		while(substRS.next()){
			listaSubs.addElement(substRS.getString("descr"));
		}
		getSubstST.close();
		substRS.close();
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
		
		btnAddtolist = new JButton("Adicionar a lista");
		btnAddtolist.addActionListener(this);
		
		JLabel lblSubstncias = new JLabel("Subst\u00E2ncias");
		
		JLabel lblSubstnciasAlrgicas = new JLabel("Subst\u00E2ncias alerg\u00EAnicas");
		
		JScrollPane scrollPane_1 = new JScrollPane();
		
		JScrollPane scrollPane_2 = new JScrollPane();
		
		btnRemList = new JButton("Remover da lista");
		btnRemList.addActionListener(this);
	
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
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
							.addComponent(lblRaca))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(483)
							.addComponent(btnVoltar)
							.addGap(39)
							.addComponent(btnCadastrar))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 277, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnVerObs))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
										.addComponent(btnAddtolist)
										.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 193, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addComponent(btnRemList)
										.addComponent(scrollPane_2, GroupLayout.PREFERRED_SIZE, 221, GroupLayout.PREFERRED_SIZE)))))
						.addComponent(lblPorte)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(66)
							.addComponent(lblSubstncias)
							.addGap(135)
							.addComponent(lblSubstnciasAlrgicas))
						.addComponent(lblDescrio)
						.addComponent(lblObs))
					.addContainerGap(10, Short.MAX_VALUE))
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
					.addGap(12)
					.addComponent(lblDescrio)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblObs)
					.addGap(22)
					.addComponent(lblPorte)
					.addGap(24)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(lblSubstnciasAlrgicas)
						.addComponent(lblSubstncias))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 133, GroupLayout.PREFERRED_SIZE)
						.addComponent(scrollPane_2, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_contentPane.createParallelGroup(Alignment.BASELINE)
							.addComponent(btnCadastrar)
							.addComponent(btnVoltar))
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
							.addComponent(btnAddtolist)
							.addComponent(btnRemList)))
					.addContainerGap())
		);
		
		listAler = new JList();
		scrollPane_2.setViewportView(listAler);
		
		listSub = new JList();
		listSub.setModel(listaSubs);
		scrollPane_1.setViewportView(listSub);
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
		
		if(evento.getSource().equals(btnAddtolist)){
			//System.out.println(listSub.getSelectedValue().toString());
			if(listSub.getSelectedValue()!=null){
				listaAler.addElement(listSub.getSelectedValue().toString());
				listaSubs.removeElement(listSub.getSelectedValue());
				listSub.setModel(listaSubs);
				listAler.setModel(listaAler);
			}
		}
		if(evento.getSource().equals(btnRemList)){
			if(listAler.getSelectedValue()!=null){
				listaSubs.addElement(listAler.getSelectedValue().toString());
				listaAler.removeElement(listAler.getSelectedValue());
				listAler.setModel(listaAler);
				listSub.setModel(listaSubs);
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
				if(listaAler.getElementAt(0) != null){
					for(int i=0; i<listaAler.size();i++){
					query = "SELECT * FROM animal WHERE nomea = '"+txtNome.getText()+"' AND cpf_cliente = '"+usuario.getCPF()+
							"' AND dt_nasc=STR_TO_DATE('"+txtDdmmaaaa.getText()+"', '%d/%m/%Y') LIMIT 1;";
					PreparedStatement getani = conec.prepareStatement(query);
					ResultSet aniRS = getani.executeQuery();
					aniRS.next();
					String query2 = "SELECT * FROM substancia WHERE descr='"+listaAler.getElementAt(i)+"' LIMIT 1;";
					PreparedStatement getsub = conec.prepareStatement(query2);
					ResultSet subRS = getsub.executeQuery();
					subRS.next();
					cadastrarST.execute("INSERT INTO alergico(id_animal,cod_substancia) VALUES ('"+aniRS.getString("id")+
							"','"+subRS.getString("cod")+"');");
					}
				}
				
				
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
