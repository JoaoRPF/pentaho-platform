/*!
 *
 * This program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 * or from the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 *
 * Copyright (c) 2002-2018 Hitachi Vantara. All rights reserved.
 *
 */

package org.pentaho.mantle.client.admin;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.pentaho.gwt.widgets.client.utils.string.StringUtils;
import org.pentaho.mantle.client.messages.Messages;
import org.pentaho.ui.xul.gwt.tags.GwtDialog;

public class ChangePasswordDialog extends GwtDialog implements ServiceCallback {

  private UpdatePasswordController controller;
  private PasswordTextBox newPasswordTextBox;
  private PasswordTextBox reTypePasswordTextBox;
  private PasswordTextBox administratorPasswordTextBox;
  private Button acceptBtn = new Button( Messages.getString( "ok" ) );
  private Button cancelBtn = new Button( Messages.getString( "cancel" ) );

  public ChangePasswordDialog( UpdatePasswordController controller ) {
    setWidth( 260 );
    setHeight( 240 );
    getButtonPanel();
    setTitle( Messages.getString( "changePassword" ) );

    acceptBtn.setEnabled( false );
    newPasswordTextBox = new PasswordTextBox();
    newPasswordTextBox.setWidth( "260px" );
    reTypePasswordTextBox = new PasswordTextBox();
    reTypePasswordTextBox.setWidth( "260px" );
    administratorPasswordTextBox = new PasswordTextBox();
    administratorPasswordTextBox.setWidth( "260px" );

    TextBoxValueChangeHandler textBoxChangeHandler = new TextBoxValueChangeHandler();
    newPasswordTextBox.addKeyUpHandler( textBoxChangeHandler );
    reTypePasswordTextBox.addKeyUpHandler( textBoxChangeHandler );
    administratorPasswordTextBox.addKeyUpHandler( textBoxChangeHandler );

    acceptBtn.setStylePrimaryName( "pentaho-button" );
    acceptBtn.addClickHandler( new AcceptListener() );
    cancelBtn.setStylePrimaryName( "pentaho-button" );
    cancelBtn.addClickHandler( new CancelListener() );

    this.controller = controller;
  }

  public Panel getButtonPanel() {
    HorizontalPanel hp = new HorizontalPanel();
    hp.add( acceptBtn );
    hp.setCellWidth( acceptBtn, "100%" );
    hp.setCellHorizontalAlignment( acceptBtn, HorizontalPanel.ALIGN_RIGHT );
    hp.add( cancelBtn );
    return hp;
  }

  public Panel getDialogContents() {

    VerticalPanel vp = new VerticalPanel();

    Label nameLabel = new Label( Messages.getString( "newPassword" ) + ":" );
    vp.add( nameLabel );
    vp.add( newPasswordTextBox );

    SimplePanel separatorSpacer = new SimplePanel();
    separatorSpacer.setStylePrimaryName( "spacer" );
    vp.add( separatorSpacer );

    Label passwordLabel = new Label( Messages.getString( "retypePassword" ) + ":" );
    vp.add( passwordLabel );
    vp.add( reTypePasswordTextBox );

    separatorSpacer = new SimplePanel();
    separatorSpacer.setStylePrimaryName( "spacer" );
    vp.add( separatorSpacer );

    separatorSpacer = new SimplePanel();
    separatorSpacer.setStylePrimaryName( "spacer" );
    separatorSpacer.addStyleDependentName( "border-top" );
    vp.add( separatorSpacer );

    Label administratorLabel = new Label( Messages.getString( "administratorPassword" ) + ":" );
    vp.add( administratorLabel );
    vp.add( administratorPasswordTextBox );

    return vp;
  }

  @Override
  public void show() {
    super.show();

    this.dialog.setStyleDependentName( "change-password", true );
  }

  @Override
  public void serviceResult( boolean ok ) {
    if ( ok ) {
      this.hide();
    } else {
      newPasswordTextBox.setText( "" );
      reTypePasswordTextBox.setText( "" );
      administratorPasswordTextBox.setText( "" );

      cancelBtn.setEnabled( true );
    }
  }

  class AcceptListener implements ClickHandler {
    public void onClick( ClickEvent event ) {
      acceptBtn.setEnabled( false );
      cancelBtn.setEnabled( false );

      String newPassword = newPasswordTextBox.getText();
      String administratorPassword = administratorPasswordTextBox.getText();

      controller.updatePassword( newPassword, administratorPassword, ChangePasswordDialog.this );
    }
  }

  class CancelListener implements ClickHandler {
    public void onClick( ClickEvent event ) {
      hide();
    }
  }

  class TextBoxValueChangeHandler implements KeyUpHandler {
    public void onKeyUp( KeyUpEvent evt ) {
      String password = newPasswordTextBox.getText();
      String reTypePassword = reTypePasswordTextBox.getText();
      String administratorPassword = administratorPasswordTextBox.getText();
      boolean isEnabled = !StringUtils.isEmpty( administratorPassword ) && !StringUtils.isEmpty( password ) && password.equals( reTypePassword );
      acceptBtn.setEnabled( isEnabled );
    }
  }
}
