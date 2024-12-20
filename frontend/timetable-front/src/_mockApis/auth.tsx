const mockUsers = [
    { email: 'admin@example.com', password: 'password123', token: 'token-admin' },
    { email: 'user@example.com', password: 'password123', token: 'token-user' }
  ];
  
export  const login = (email: string, password: string): Promise<{ token: string }> => {
    return new Promise((resolve) => {
      const user = mockUsers.find(user => user.email === email && user.password === password);
      if (user) {
        resolve({ token: user.token });
      } 
    });
  };