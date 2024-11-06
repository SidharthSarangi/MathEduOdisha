// src/hooks/useAuth.ts
import { useState, useEffect } from 'react';
import { loginUser, signupUser } from '../api/authApi' ;

interface User {
  id: string;
  email: string;
  name?: string;
}

export const useAuth = () => {
  const [user, setUser] = useState<User | null>(null);
  const [error, setError] = useState<string | null>(null);

  // Load user from localStorage on initial render
  useEffect(() => {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      setUser(JSON.parse(storedUser));
    }
  }, []);

  const login = async (email: string, password: string) => {
    try {
      const userData = await loginUser(email, password);
      setUser(userData.user);
      setError(null);
    } catch (error) {
      setError("Invalid login credentials");
    }
  };

  const signup = async (email: string, password: string) => {
    try {
      const userData = await signupUser(email, password);
      setUser(userData.user);
      setError(null);
    } catch (error) {
      setError("Signup failed");
    }
  };

  const logout = () => {
    localStorage.removeItem('user');
    localStorage.removeItem('token');
    setUser(null);
  };

  return { user, error, login, signup, logout };
};
