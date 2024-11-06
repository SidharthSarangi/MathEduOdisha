import axios from 'axios';

export const loginUser = async (email: string, password: string) => {
  try {
    const response = await axios.post('/api/auth/login', { email, password });
    const { user, token } = response.data;

    // Save user and token in localStorage
    localStorage.setItem('user', JSON.stringify(user));
    localStorage.setItem('token', token);

    return response.data;
  } catch (error) {
    throw new Error('Login failed');
  }
};

export const signupUser = async (email: string, password: string) => {
  try {
    const response = await axios.post('/api/auth/signup', { email, password });
    const { user, token } = response.data;

    // Save user and token in localStorage
    localStorage.setItem('user', JSON.stringify(user));
    localStorage.setItem('token', token);

    return response.data;
  } catch (error) {
    throw new Error('Signup failed');
  }
};
