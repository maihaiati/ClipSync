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
		SuspendLayout()
		' 
		' richData
		' 
		richData.Location = New Point(12, 12)
		richData.Name = "richData"
		richData.Size = New Size(491, 272)
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
		' MainWindow
		' 
		AutoScaleDimensions = New SizeF(7F, 15F)
		AutoScaleMode = AutoScaleMode.Font
		ClientSize = New Size(515, 335)
		Controls.Add(btnChangeInfo)
		Controls.Add(btnSend)
		Controls.Add(richData)
		Name = "MainWindow"
		Text = "ClipSync"
		ResumeLayout(False)
	End Sub
	Friend WithEvents notiIcon As NotifyIconManager
	Friend WithEvents richData As RichTextBox
	Friend WithEvents btnSend As Button
	Friend WithEvents btnChangeInfo As Button

End Class
