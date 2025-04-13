<Global.Microsoft.VisualBasic.CompilerServices.DesignerGenerated()> _
Partial Class InfoChange
	Inherits System.Windows.Forms.Form

	'Form overrides dispose to clean up the component list.
	<System.Diagnostics.DebuggerNonUserCode()> _
	Protected Overrides Sub Dispose(ByVal disposing As Boolean)
		Try
			If disposing AndAlso components IsNot Nothing Then
				components.Dispose()
			End If
		Finally
			MyBase.Dispose(disposing)
		End Try
	End Sub

	'Required by the Windows Form Designer
	Private components As System.ComponentModel.IContainer

	'NOTE: The following procedure is required by the Windows Form Designer
	'It can be modified using the Windows Form Designer.  
	'Do not modify it using the code editor.
	<System.Diagnostics.DebuggerStepThrough()> _
	Private Sub InitializeComponent()
		txtUsername = New TextBox()
		txtOldPass = New TextBox()
		txtNewPass = New TextBox()
		btnChangeUser = New Button()
		btnChangePass = New Button()
		txtNewPassConfirm = New TextBox()
		SuspendLayout()
		' 
		' txtUsername
		' 
		txtUsername.Location = New Point(31, 22)
		txtUsername.Name = "txtUsername"
		txtUsername.PlaceholderText = "Tên người dùng"
		txtUsername.Size = New Size(320, 23)
		txtUsername.TabIndex = 0
		' 
		' txtOldPass
		' 
		txtOldPass.Location = New Point(31, 62)
		txtOldPass.Name = "txtOldPass"
		txtOldPass.PasswordChar = "*"c
		txtOldPass.PlaceholderText = "Mật khẩu cũ"
		txtOldPass.Size = New Size(320, 23)
		txtOldPass.TabIndex = 1
		' 
		' txtNewPass
		' 
		txtNewPass.Location = New Point(31, 105)
		txtNewPass.Name = "txtNewPass"
		txtNewPass.PasswordChar = "*"c
		txtNewPass.PlaceholderText = "Mật khẩu mới"
		txtNewPass.Size = New Size(320, 23)
		txtNewPass.TabIndex = 2
		' 
		' btnChangeUser
		' 
		btnChangeUser.Location = New Point(54, 210)
		btnChangeUser.Name = "btnChangeUser"
		btnChangeUser.Size = New Size(92, 23)
		btnChangeUser.TabIndex = 3
		btnChangeUser.Text = "Cập nhật tên"
		btnChangeUser.UseVisualStyleBackColor = True
		' 
		' btnChangePass
		' 
		btnChangePass.Location = New Point(188, 210)
		btnChangePass.Name = "btnChangePass"
		btnChangePass.Size = New Size(134, 23)
		btnChangePass.TabIndex = 4
		btnChangePass.Text = "Cập nhật mật khẩu"
		btnChangePass.UseVisualStyleBackColor = True
		' 
		' txtNewPassConfirm
		' 
		txtNewPassConfirm.Location = New Point(31, 148)
		txtNewPassConfirm.Name = "txtNewPassConfirm"
		txtNewPassConfirm.PasswordChar = "*"c
		txtNewPassConfirm.PlaceholderText = "Xác nhận mật khẩu mới"
		txtNewPassConfirm.Size = New Size(320, 23)
		txtNewPassConfirm.TabIndex = 5
		' 
		' InfoChange
		' 
		AutoScaleDimensions = New SizeF(7F, 15F)
		AutoScaleMode = AutoScaleMode.Font
		ClientSize = New Size(386, 260)
		Controls.Add(txtNewPassConfirm)
		Controls.Add(btnChangePass)
		Controls.Add(btnChangeUser)
		Controls.Add(txtNewPass)
		Controls.Add(txtOldPass)
		Controls.Add(txtUsername)
		Name = "InfoChange"
		Text = "InfoChange"
		ResumeLayout(False)
		PerformLayout()
	End Sub

	Friend WithEvents txtUsername As TextBox
	Friend WithEvents txtOldPass As TextBox
	Friend WithEvents txtNewPass As TextBox
	Friend WithEvents btnChangeUser As Button
	Friend WithEvents btnChangePass As Button
	Friend WithEvents txtNewPassConfirm As TextBox
End Class
