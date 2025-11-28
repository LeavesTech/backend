import { IAuthUser, NewAuthUser } from './auth-user.model';

export const sampleWithRequiredData: IAuthUser = {
  id: 32391,
  username: 'unlike anenst pastel',
  email: 'Joannie69@gmail.com',
  firstName: 'Carol',
  lastName: 'Lesch',
  password: 'searchingly above aircraft',
  enabled: false,
  userType: 'ADMIN',
};

export const sampleWithPartialData: IAuthUser = {
  id: 8768,
  username: 'for bonfire labourer',
  email: 'Clay.Lemke@yahoo.com',
  firstName: 'Eliseo',
  lastName: 'Hauck',
  password: 'instead and',
  enabled: false,
  userType: 'TEACHER',
};

export const sampleWithFullData: IAuthUser = {
  id: 11338,
  username: 'fooey although',
  email: 'Shayne_Hoeger@gmail.com',
  phoneNumber: 'hoarse',
  firstName: 'Christina',
  lastName: 'Jaskolski',
  password: 'vamoose kissingly',
  enabled: false,
  userType: 'STUDENT',
};

export const sampleWithNewData: NewAuthUser = {
  username: 'insolence um positively',
  email: 'Bryon.Skiles@yahoo.com',
  firstName: 'Josefina',
  lastName: 'Wehner',
  password: 'airport favorite gah',
  enabled: true,
  userType: 'ADMIN',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
