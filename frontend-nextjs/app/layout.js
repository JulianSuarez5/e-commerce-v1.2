'use client';

import { Inter } from "next/font/google";
import "./globals.css";
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { useState, useEffect } from 'react';

const inter = Inter({ subsets: ["latin"] });

export default function RootLayout({ children }) {
  const [user, setUser] = useState(null);
  const router = useRouter();

  useEffect(() => {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      setUser(JSON.parse(storedUser));
    }
  }, []);

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    setUser(null);
    router.push('/');
  };

  return (
    <html lang="en">
      <body className={inter.className}>
        <nav className="bg-gray-800 p-4">
          <div className="container mx-auto flex justify-between items-center">
            <div className="flex items-center">
              <Link href="/" className="text-white font-bold mr-4">Home</Link>
              <Link href="/products" className="text-white">Products</Link>
            </div>
            <div>
              {user ? (
                <div className="flex items-center">
                  <span className="text-white mr-4">Welcome, {user.name}</span>
                  <button onClick={handleLogout} className="bg-red-500 hover:bg-red-700 text-white font-bold py-2 px-4 rounded">
                    Logout
                  </button>
                </div>
              ) : (
                <div>
                  <Link href="/login" className="text-white mr-4">Login</Link>
                  <Link href="/register" className="text-white">Register</Link>
                </div>
              )}
            </div>
          </div>
        </nav>
        <main>{children}</main>
      </body>
    </html>
  );
}
