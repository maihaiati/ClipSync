Imports System.IO

Public Class NotifyIconManager
    Private notifyIcon As NotifyIcon
    Private contextMenu As ContextMenuStrip
    Private mainForm As MainWindow
    Public Sub New(mainForm As MainWindow)
        Me.mainForm = mainForm
        notifyIcon = New NotifyIcon()
        Using ms As New MemoryStream(My.Resources.Resources.LogoClipSync_NoBg)
            notifyIcon.Icon = New Icon(ms)
        End Using
        notifyIcon.Text = "ClipSync"
        notifyIcon.Visible = True

        contextMenu = New ContextMenuStrip()
        contextMenu.Items.Add("Mở", Nothing, AddressOf ShowForm)
        contextMenu.Items.Add("Thoát", Nothing, AddressOf ExitApp)
        notifyIcon.ContextMenuStrip = contextMenu

        AddHandler notifyIcon.MouseClick, AddressOf NotifyIcon_Click

        AddHandler mainForm.FormClosing, AddressOf mainForm_Closing
    End Sub

    ' Khi click vào icon thì mở lại Form
    Private Sub NotifyIcon_Click(sender As Object, e As MouseEventArgs)
        If e.Button = MouseButtons.Left Then
            ShowForm(Nothing, Nothing)
        End If
    End Sub

    Private Sub ShowForm(sender As Object, e As EventArgs)
        mainForm.Show()
        mainForm.WindowState = FormWindowState.Normal
        mainForm.Activate()
    End Sub
    Private Sub ExitApp(sender As Object, e As EventArgs)
        notifyIcon.Dispose()
        ' Ngừng lắng nghe clipboard khi đóng form
        mainForm.enableSync(False)
        Application.Exit()
    End Sub

    ' Ngăn người dùng tắt ứng dụng khi nhấn "X", chỉ ẩn Form
    Private Sub mainForm_Closing(sender As Object, e As FormClosingEventArgs)
        If e.CloseReason = CloseReason.UserClosing Then
            e.Cancel = True
            mainForm.Hide()
            notifyIcon.ShowBalloonTip(1000, "ClipSync", "Nhấp vào icon để mở lại", ToolTipIcon.Info)
        End If
    End Sub
End Class
