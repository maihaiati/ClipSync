Imports System.ComponentModel

Public Class InfoChange
	Private mainForm As MainWindow

	Public Sub New(mainForm As MainWindow)

		' This call is required by the designer.
		InitializeComponent()

		' Add any initialization after the InitializeComponent() call.
		Me.mainForm = mainForm
	End Sub

	Private Sub InfoChange_Closing(sender As Object, e As CancelEventArgs) Handles Me.Closing
		mainForm.Show()
	End Sub
End Class