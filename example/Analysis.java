/** Implement the framework for an application using SciTK.
 * The idea here is that a user, writing an application, can
 * implement SciTK_App and take advantage of generic UI elements here.
 * 
 * @class Analysis
 * @brief Example application
 * @author Alex Zylstra
 * @date 2013/04/20
 * @copyright Alex Zylstra
 * @license MIT
 */

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import java.io.File;
import java.util.Random;

import SciTK.*;

public class Analysis extends SciTK_App
{
    protected Plot_XY plot2;
    protected SciTK_Image plot1;
    protected boolean flip = false;

    /** Constructor */
	public Analysis()
	{
        // use constructor for superclass SciTK_App with specified size:
        super(200,200);
        NAME = "Test App";
        AUTHOR = "Alex Zylstra";
        DATE = "Apr 18, 2013";
        String logo_path = "resources/avogadro.png";
        about_icon = new ImageIcon(getClass().getResource(logo_path));
        tray_icon = new ImageIcon(getClass().getResource(logo_path));

        initUI();
        app_ui();
	}
    public void open_file() {return;}

    /** Load the initial UI */
	public void app_ui()
	{
        // Set a bit of text:
        JLabel instruction = new JLabel(" Demo plots: ");
        instruction.setFont(new Font("Serif", Font.BOLD, 13));
        instruction.setAlignmentX(0.5f);
        add(instruction);

        // plot button
        JButton plot = new JButton("Plot");
        plot.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                // test image plot:
                float[][] image_data = new float[100][100];
                for(int i=0; i<100;i++)
                {
                    for(int j=0;j<100;j++)
                    {
                        image_data[i][j] = (float)(i*100.+j);
                    }
                }
                plot1 = new SciTK_Image(image_data,"test2");
                wm.add(plot1);

                // test xy plot:
                float[][] testdata = { {0.1f, 0.4f, 0.3f}, {1f, 2f, 3f} };
                double[][][] testdata2 = {{{0.1, 0.4, 0.3}, {1, 2, 3} } , { {0.1, 0.4, 0.3}, {2, 1, 2} }};
                String[] names2 = {"1","2"};

                plot2 = new Plot_XY(testdata,"test","x","y","test");
                wm.add(plot2);
                Plot_XYLine plot2b = new Plot_XYLine(testdata2,names2,"x","y","test");
                wm.add(plot2b);
                Plot_XYStep plot2c = new Plot_XYStep(testdata2,names2,"x","y","test");
                wm.add(plot2c);

                // test image plot:
                int[][] image_data2 = new int[100][100];
                for(int i=0; i<100;i++)
                {
                    for(int j=0;j<100;j++)
                    {
                        image_data2[i][j] = (int) ( 256.*((float)i*100.+(float)j)/(10000.));
                    }
                }
                SciTK_Image plot3 = new SciTK_Image(image_data2,"test3");
                wm.add(plot3);

                double[][] testerr = { {1,2,3} , {0.9,1.9,2.9} , {1.1,2.1,3.1} , {1,2,3} , {0.5,1.5,2.5} , {1.2,2.2,3.2} };
                Plot_XYError plot4 = new Plot_XYError(testerr,"test","x","y","test");
                wm.add(plot4);

                double[][] testxyz = { {1,2,3,1,2,3,1,2,3} , {1,1,1,2,2,2,3,3,3} , {7,2,4,1,7,9,6,4,8} };
                Plot_XYZBlock plot5 = new Plot_XYZBlock(testxyz,"test","x","y","test");
                wm.add(plot5);
            }
        });
        plot.setAlignmentX(0.5f);
        add(plot);

        // prompt button
        JButton prompt = new JButton("prompt");
        prompt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                // test multi prompt
                Prompt_Value[] vals = new Prompt_Value[3];
                vals[0] = new Prompt_Value("","test1");
                vals[1] = new Prompt_Value("","test2");
                vals[2] = new Prompt_Value("","test3");
                Dialog_MultiPrompt p = new Dialog_MultiPrompt(vals,"Test MultiPrompt");
            }
        });
        prompt.setAlignmentX(0.5f);
        add(prompt);

        // randomize plot button
        JButton random = new JButton("randomize!");
        random.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                // make random data for plot2:
                Random r = new Random();
                double[][] testdata = { {r.nextDouble(), r.nextDouble(), r.nextDouble()}, {r.nextDouble(), r.nextDouble(), r.nextDouble()} };
                plot2.update_data(testdata,"test");
            }
        });
        random.setAlignmentX(0.5f);
        add(random);

        // flip image button
        JButton flip_button = new JButton("flip image!");
        flip_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                // test image plot:
                float[][] image_data = new float[100][100];
                for(int i=0; i<100;i++)
                {
                    for(int j=0;j<100;j++)
                    {
                        if(flip)
                            image_data[i][j] = -(float)(i*100.+j);
                        else
                            image_data[i][j] = (float)(i*100.+j);
                    }
                }
                flip = !flip;
                plot1.update_data(image_data);
            }
        });
        flip_button.setAlignmentX(0.5f);
        add(flip_button);
	}

    /** Main method */
	public static void main(String[] args)
	{
        // Create a new Analysis object and let
        // Java invoke it when appropriate:
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Analysis a = new Analysis();
				a.setVisible(true);
			}
		});
	} 
}