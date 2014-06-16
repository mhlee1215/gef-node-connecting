package org.ssu.ml.presentation;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.plaf.basic.BasicTabbedPaneUI.TabbedPaneLayout;


public class CloseableTabbedPaneUI extends BasicTabbedPaneUI

{

	// override to return our layoutmanager

	protected LayoutManager createLayoutManager()

	{

		return new TestPlafLayout();

	}

	// add 40 to the tab size to allow room for the close button and 8 to the
	// height

	protected Insets getTabInsets(int tabPlacement, int tabIndex)

	{

		// note that the insets that are returned to us are not copies.

		Insets defaultInsets = (Insets) super.getTabInsets(tabPlacement,
				tabIndex).clone();

		defaultInsets.right += 50;

		// defaultInsets.top += 4;

		// defaultInsets.bottom += 4;

		return defaultInsets;

	}

	class TestPlafLayout extends TabbedPaneLayout

	{

		// a list of our close buttons

		java.util.ArrayList closeButtons = new java.util.ArrayList();

		public void layoutContainer(Container parent)

		{

			super.layoutContainer(parent);

			// ensure that there are at least as many close buttons as tabs

			while (tabPane.getTabCount() > closeButtons.size())

			{

				CloseButton button = new CloseButton(closeButtons.size());
				
				closeButtons.add(button);

			}

			Rectangle rect = new Rectangle();

			int i;

			for (i = 0; i < tabPane.getTabCount(); i++)

			{

				rect = getTabBounds(i, rect);

				JButton closeButton = (JButton) closeButtons.get(i);
				closeButton.setText("x");
				closeButton.setFont( new Font("Lucida Grande", Font.BOLD, 11));
				// shift the close button 3 down from the top of the pane and 20
				// to the left

				closeButton.setLocation(rect.x + rect.width - 30, rect.y + 2);

				closeButton.setSize(22, 20);

				tabPane.add(closeButton);

			}

			for (; i < closeButtons.size(); i++)

			{

				// remove any extra close buttons

				tabPane.remove((JButton) closeButtons.get(i));

			}

		}

		// implement UIResource so that when we add this button to the

		// tabbedpane, it doesn't try to make a tab for it!

		class CloseButton extends JButton implements
				javax.swing.plaf.UIResource

		{

			public CloseButton(int index)

			{

				super(new CloseButtonAction(index));
				setToolTipText("Close this tab");

				// remove the typical padding for the button

				setMargin(new Insets(-3, 0, 0, 0));

				addMouseListener(new MouseAdapter()

				{

					public void mouseEntered(MouseEvent e)

					{

						//setFont( new Font("Lucida Grande", Font.BOLD, 11));
						setForeground(new Color(255, 0, 0));
						//updateUI();

					}

					public void mouseExited(MouseEvent e)

					{

						//setFont( new Font("Lucida Grande", Font.PLAIN, 11));
						setForeground(new Color(0, 0, 0));
						//updateUI();
					}

				});

			}

		}

		class CloseButtonAction extends AbstractAction

		{

			int index;

			public CloseButtonAction(int index)

			{

				super("x");

				this.index = index;

			}

			public void actionPerformed(ActionEvent e)

			{
				System.out.println("remove tab index : "+index);
				tabPane.remove(index);

			}

		} // End of CloseButtonAction

	} // End of TestPlafLayout

} // End of static class TestPlaf