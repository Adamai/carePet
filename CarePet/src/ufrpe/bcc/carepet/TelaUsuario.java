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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class TelaUsuario extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JButton btnCadastrarAnimal;
	private JButton btnVoltar;
	private Cliente usuario;
	private DefaultListModel model = new DefaultListModel();
	private JList list = new JList();
	private JButton btnRem;
	private int ilista=0;
	private JLabel Nomea;
	private JLabel Idadea;
	private JLabel Descra;
	private JLabel Obsa;
	private JLabel lblAl;
	private JTextField textMes;
	private JButton btnFatura;
	
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
		setBounds(100, 100, 1200, 600);
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
		

		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
	            if (!arg0.getValueIsAdjusting()) {
	              Nomea.setText(list.getSelectedValue().toString());
	      		try{
	    			Connection conex = getConexao();
	    			Statement getRaca = conex.createStatement();
	    			ResultSet rsGetAni = getRaca.executeQuery("SELECT * FROM animal WHERE cpf_cliente="+user.getCPF()+" AND nomea='"+list.getSelectedValue().toString()+"' LIMIT 1;");
	    			rsGetAni.next();
	    			String idani = rsGetAni.getString("id");
	    			String query = "SELECT * FROM raça WHERE cod="+rsGetAni.getString("cod_raça")+";";
	    			PreparedStatement psGetRaca = conex.prepareStatement(query);
	    			ResultSet rsGetRaca = psGetRaca.executeQuery();
	    			rsGetRaca.next();
	    			Idadea.setText("Idade: "+rsGetAni.getString("idade"));
	    			Descra.setText(rsGetRaca.getString("descr"));
	    			Obsa.setText(rsGetRaca.getString("obs"));
	    			rsGetAni = getRaca.executeQuery("SELECT * FROM alergico WHERE id_animal="+idani+" ;");
	    			StringBuffer alergenos = new StringBuffer();
	    			while(rsGetAni.next()){
	    				query = "SELECT * FROM substancia WHERE cod="+rsGetAni.getString("cod_substancia")+" LIMIT 1;";
	    				psGetRaca = conex.prepareStatement(query);
	    				rsGetRaca = psGetRaca.executeQuery();
	    				rsGetRaca.next();
	    				alergenos.append(rsGetRaca.getString("descr")+" | ");
	    			}
	    			lblAl.setText("Alérgenos: "+alergenos.toString());
	    			rsGetRaca.close();
	    			rsGetAni.close();
	    			psGetRaca.close();
	    			conex.close();
	    		} catch (SQLException e) {
	    			System.out.println("Houve erro");
	    			e.printStackTrace();
	    		}
	            }
	        }
		});
		
		btnCadastrarAnimal = new JButton("Cadastrar Animal");
		btnCadastrarAnimal.addActionListener(this);
		
		btnVoltar = new JButton("Voltar");
		btnVoltar.addActionListener(this);
		
		JScrollPane scrollPane = new JScrollPane();
		
		btnRem = new JButton("Remover Animal");
		btnRem.addActionListener(this);
		
		Nomea = new JLabel("nome");
		
		Idadea = new JLabel("Idade: ");
		
		Descra = new JLabel("Descri\u00E7\u00E3o");
		
		Obsa = new JLabel("Obs");
		
		lblAl = new JLabel("Al\u00E9rgenos:");
		
		btnFatura = new JButton("Gerar Fatura Mensal de Agendamentos");
		btnFatura.addActionListener(this);
		
		textMes = new JTextField();
		textMes.setColumns(10);
		
		JLabel lblMes = new JLabel("M\u00EAs(01-12/aaaa):");
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(btnVoltar)
					.addContainerGap(1113, Short.MAX_VALUE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(btnRem))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(26)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(btnCadastrarAnimal)
								.addComponent(btnFatura))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblMes)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(textMes, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(268)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(Nomea)
								.addComponent(scrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE)
								.addComponent(Idadea)
								.addComponent(Descra)
								.addComponent(Obsa)
								.addComponent(lblAl, GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE))
							.addGap(0)))
					.addGap(21))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(48)
					.addComponent(btnCadastrarAnimal)
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnFatura)
						.addComponent(lblMes)
						.addComponent(textMes, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 417, Short.MAX_VALUE)
					.addComponent(btnVoltar))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(57)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 206, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnRem)
					.addGap(28)
					.addComponent(Nomea)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(Idadea)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(Descra)
					.addGap(18)
					.addComponent(Obsa)
					.addGap(18)
					.addComponent(lblAl, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(85, Short.MAX_VALUE))
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
		if(evento.getSource().equals(btnFatura)){
			float fatmen = 0;
			int laterd =0;
			int laterm=0;
			int latera = 0;
			try{
				Connection conex = getConexao();
				Statement sgetFats = conex.createStatement();
				ResultSet rsgetFats = sgetFats.executeQuery("SELECT * FROM animal WHERE cpf_cliente='"+usuario.getCPF()+
						"' ;");
				while(rsgetFats.next()){
					PreparedStatement psgetEnv = conex.prepareStatement("SELECT * FROM envolve where id_animal="+rsgetFats.getString("id")+" ;");
					ResultSet rsgetEnv = psgetEnv.executeQuery();
					while(rsgetEnv.next()){
						PreparedStatement psgetAgen = conex.prepareStatement("SELECT * FROM agendamento where id="+rsgetEnv.getString("id_agend")+" AND"
								+ " YEAR(dt_agenda)="+textMes.getText().substring(3)+" AND MONTH(dt_agenda)="+textMes.getText().substring(0, 2)+";");
						ResultSet rsgetAgen = psgetAgen.executeQuery();
						Statement slater = conex.createStatement();
						ResultSet rslater = slater.executeQuery("SELECT * FROM agendamento where id="+rsgetEnv.getString("id_agend")+";");
						while(rslater.next()){
							if(Integer.parseInt(rslater.getString("dt_agenda").substring(0,4))>=latera &&
							Integer.parseInt(rslater.getString("dt_agenda").substring(5,7))>=laterm &&
							Integer.parseInt(rslater.getString("dt_agenda").substring(8,10))>laterd){
								latera=Integer.parseInt(rslater.getString("dt_agenda").substring(0,4));
								laterm=Integer.parseInt(rslater.getString("dt_agenda").substring(8,10));
								laterd=Integer.parseInt(rslater.getString("dt_agenda").substring(8,10));
											}
						}
						while(rsgetAgen.next()){
							PreparedStatement psgetFat = conex.prepareStatement("SELECT * FROM fatura WHERE id_cod="+rsgetAgen.getString("cod_fatura")+" AND"
									+ " stats!='pagamento confirmado';");
							ResultSet rsgetFat = psgetFat.executeQuery();
							while(rsgetFat.next()){
								fatmen = fatmen + Float.parseFloat(rsgetFat.getString("vl_total"));
							} rsgetFat.close();psgetFat.close();
						}rsgetAgen.close();psgetAgen.close();
					}rsgetEnv.close();psgetEnv.close();
				} rsgetFats.close();conex.close();
				if(fatmen>0){ //gerar fatura
					Statement setFat = conex.createStatement();
					setFat.executeQuery("INSERT INTO fatura(stats, vl_total, dt_vencimento) VALUES("
						+ "'aguardando confirmação de pagamento',"+
							fatmen+",STR_TO_DATE('"+laterd+"/"+laterm+"/"+latera+"', '%d/%m/%y');");
					JOptionPane.showMessageDialog(null, "Fatura gerada");
					setFat.close();
				}
			}  catch (Exception e) {
				e.printStackTrace();
			}
		}
		/*if(evento.getSource().equals(btnCadastrarAgendamento)){
			dispose();
			TelaAgendamento tela = new TelaAgendamento(usuario);
			tela.setVisible(true);
		}*/
	}
}
