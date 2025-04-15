<Global.Microsoft.VisualBasic.CompilerServices.DesignerGenerated()>
Partial Class MainWindow
    Inherits System.Windows.Forms.Form

    'Form overrides dispose to clean up the component list.
    <System.Diagnostics.DebuggerNonUserCode()>
    Protected Overrides Sub Dispose(disposing As Boolean)
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
	<System.Diagnostics.DebuggerStepThrough()>
	Private Sub InitializeComponent()
		richData = New RichTextBox()
		btnSend = New Button()
		btnChangeInfo = New Button()
		btnEnable = New Button()
		Label1 = New Label()
		SuspendLayout()
		' 
		' richData
		' 
		richData.Location = New Point(12, 44)
		richData.Name = "richData"
		richData.Size = New Size(491, 240)
		richData.TabIndex = 0
		richData.Text = ""
		' 
		' btnSend
		' 
		btnSend.Location = New Point(130, 300)
		btnSend.Name = "btnSend"
		btnSend.Size = New Size(75, 23)
		btnSend.TabIndex = 1
		btnSend.Text = "Gửi"
		btnSend.UseVisualStyleBackColor = True
		' 
		' btnChangeInfo
		' 
		btnChangeInfo.Location = New Point(261, 300)
		btnChangeInfo.Name = "btnChangeInfo"
		btnChangeInfo.Size = New Size(139, 23)
		btnChangeInfo.TabIndex = 2
		btnChangeInfo.Text = "Thay đổi thông tin"
		btnChangeInfo.UseVisualStyleBackColor = True
		' 
		' btnEnable
		' 
		btnEnable.Location = New Point(185, 8)
		btnEnable.Name = "btnEnable"
		btnEnable.Size = New Size(75, 23)
		btnEnable.TabIndex = 3
		btnEnable.Text = "Bật"
		btnEnable.UseVisualStyleBackColor = True
		' 
		' Label1
		' 
		Label1.AutoSize = True
		Label1.Location = New Point(12, 12)
		Label1.Name = "Label1"
		Label1.Size = New Size(148, 15)
		Label1.TabIndex = 4
		Label1.Text = "Bật đồng bộ hoá clipboard"
		' 
		' MainWindow
		' 
		AutoScaleDimensions = New SizeF(7F, 15F)
		AutoScaleMode = AutoScaleMode.Font
		ClientSize = New Size(515, 335)
		Controls.Add(Label1)
		Controls.Add(btnEnable)
		Controls.Add(btnChangeInfo)
		Controls.Add(btnSend)
		Controls.Add(richData)
		Name = "MainWindow"
		Text = "ClipSync"
		ResumeLayout(False)
		PerformLayout()
	End Sub
	Friend WithEvents notiIcon As NotifyIconManager
	Friend WithEvents richData As RichTextBox
	Friend WithEvents btnSend As Button
	Friend WithEvents btnChangeInfo As Button
	Friend WithEvents btnEnable As Button
	Friend WithEvents Label1 As Label

End Class
