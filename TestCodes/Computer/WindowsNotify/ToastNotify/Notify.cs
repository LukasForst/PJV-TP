using Microsoft.Win32;
using System;
using System.IO;
using Windows.Data.Xml.Dom;
using Windows.UI.Notifications;

namespace ToastNotify
{
    class Notify
    {
        static void Main(string[] args)
        {
            String nameOfApp = "Notification", headline = "", text = "", iconPath = "";

            if (!IsWindows10())
            {
                Console.WriteLine("This system is not supported!");
                Environment.Exit(101);
            }


            for (int i = 0; i < args.Length; i++)
            {
                try
                {
                    if (args[i].Equals("-t"))
                    {
                        text = args[i + 1];
                    } else if (args[i].Equals("-h"))
                    {
                        headline = args[i + 1];
                    } else if (args[i].Equals("-i"))
                    {
                        iconPath = args[i + 1];
                    } else if (args[i].Equals("-n"))
                    {
                        nameOfApp = args[i + 1];
                    } else if (args[i].Equals("--help"))
                    {
                        Console.WriteLine(
                            "Mandatory switches are: -t, -h\n\r\t-h - headline - displayed bold\n\r\t-t - text\n\r\t" +
                            "-i - icon - adds icon to the toast\n\r\t-n - name of the app" +
                            "@author - Lukas Forst"
                            );
                        Environment.Exit(0);
                    }
                }
                catch
                {
                    Console.WriteLine("wrong args, use --help for help");
                    Environment.Exit(100);
                }
            }

            if(text.Equals("") || headline.Equals(""))
            {
                Console.WriteLine("wrong args, use --help for help");
                Environment.Exit(100);
            }

            XmlDocument toastXml = ToastNotificationManager.GetTemplateContent(ToastTemplateType.ToastImageAndText04);

            XmlNodeList stringElements = toastXml.GetElementsByTagName("text");
            stringElements[0].AppendChild(toastXml.CreateTextNode(headline));
            stringElements[1].AppendChild(toastXml.CreateTextNode(text));
               
            if (!iconPath.Equals(""))
            {
                String imagePath = "file:///" + Path.GetFullPath(iconPath);
                XmlNodeList imageElements = toastXml.GetElementsByTagName("image");
                imageElements[0].Attributes.GetNamedItem("src").NodeValue = imagePath;
            }

            ToastNotification toast = new ToastNotification(toastXml);
            ToastNotificationManager.CreateToastNotifier(nameOfApp).Show(toast);
        }

        static bool IsWindows10()
        {
            //@author Mitat Koyuncu - http://stackoverflow.com/questions/31885302/how-can-i-detect-if-my-app-is-running-on-windows-10

            var reg = Registry.LocalMachine.OpenSubKey(@"SOFTWARE\Microsoft\Windows NT\CurrentVersion");

            string productName = (string)reg.GetValue("ProductName");

            return productName.Contains("Windows 10");
        }
    }
}
