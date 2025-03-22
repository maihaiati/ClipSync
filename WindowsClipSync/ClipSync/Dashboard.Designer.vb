<Global.Microsoft.VisualBasic.CompilerServices.DesignerGenerated()>
Partial Class Dashboard
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
		TextBox1 = New TextBox()
		PictureBox1 = New PictureBox()
		CType(PictureBox1, ComponentModel.ISupportInitialize).BeginInit()
		SuspendLayout()
		' 
		' TextBox1
		' 
		TextBox1.Location = New Point(101, 12)
		TextBox1.Multiline = True
		TextBox1.Name = "TextBox1"
		TextBox1.Size = New Size(607, 129)
		TextBox1.TabIndex = 0
		' 
		' PictureBox1
		' 
		PictureBox1.Location = New Point(81, 161)
		PictureBox1.Name = "PictureBox1"
		PictureBox1.Size = New Size(639, 277)
		PictureBox1.TabIndex = 1
		PictureBox1.TabStop = False
		' 
		' Form1
		' 
		AutoScaleDimensions = New SizeF(7F, 15F)
		AutoScaleMode = AutoScaleMode.Font
		ClientSize = New Size(800, 450)
		Controls.Add(PictureBox1)
		Controls.Add(TextBox1)
		Name = "Form1"
		Text = "Form1"
		CType(PictureBox1, ComponentModel.ISupportInitialize).EndInit()
		ResumeLayout(False)
		PerformLayout()
	End Sub

	Friend WithEvents TextBox1 As TextBox
	Friend WithEvents PictureBox1 As PictureBox

End Class
