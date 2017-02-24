import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class SCGraphique extends JFrame{
	protected JPanel panneau, goP;
	protected JButton image, nbColonnes, nomSortie, go;
	protected String path, sortie, nombreCol;
	
	public SCGraphique(){
		nombreCol = "50";
		sortie = "default";
		path = "default";
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.panneau = new JPanel();
		this.goP = new JPanel();
		
		image = new JButton("Image");
		nbColonnes = new JButton("Nombre colonnes");
		nomSortie = new JButton("Nom fichier sortie");
		
		
		image.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
					JFileChooser dialogue = new JFileChooser();
					dialogue.showOpenDialog(null);
					path = dialogue.getSelectedFile().getPath();
				}
		});
		
		nbColonnes.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
					nombreCol = JOptionPane.showInputDialog("Entrez le nombre de colonnes à supprimer");		
				}
		});
		
		nomSortie.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
					sortie = JOptionPane.showInputDialog("Entrez le nom du fichier de sortie");
				}
		});
		
		
		panneau.add(image);
		panneau.add(nbColonnes);
		panneau.add(nomSortie);
		
		go = new JButton("Go");

		go.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
					SeamCarving launch = new SeamCarving();
					String[] toto = {path, nombreCol, sortie};
					launch.main(toto);
				}
		});
		
		goP.add(go);
		
		add(goP, BorderLayout.SOUTH);
		add(panneau, BorderLayout.CENTER);
		
		pack() ;
        setVisible(true);
	}
	
	public static void main(String[] args) {
        new SCGraphique() ;
    }
}
