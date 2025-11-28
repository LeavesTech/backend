import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAuthUser, NewAuthUser } from '../auth-user.model';

export type PartialUpdateAuthUser = Partial<IAuthUser> & Pick<IAuthUser, 'id'>;

export type EntityResponseType = HttpResponse<IAuthUser>;
export type EntityArrayResponseType = HttpResponse<IAuthUser[]>;

@Injectable({ providedIn: 'root' })
export class AuthUserService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/auth-users');

  create(authUser: NewAuthUser): Observable<EntityResponseType> {
    return this.http.post<IAuthUser>(this.resourceUrl, authUser, { observe: 'response' });
  }

  update(authUser: IAuthUser): Observable<EntityResponseType> {
    return this.http.put<IAuthUser>(`${this.resourceUrl}/${this.getAuthUserIdentifier(authUser)}`, authUser, { observe: 'response' });
  }

  partialUpdate(authUser: PartialUpdateAuthUser): Observable<EntityResponseType> {
    return this.http.patch<IAuthUser>(`${this.resourceUrl}/${this.getAuthUserIdentifier(authUser)}`, authUser, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAuthUser>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAuthUser[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAuthUserIdentifier(authUser: Pick<IAuthUser, 'id'>): number {
    return authUser.id;
  }

  compareAuthUser(o1: Pick<IAuthUser, 'id'> | null, o2: Pick<IAuthUser, 'id'> | null): boolean {
    return o1 && o2 ? this.getAuthUserIdentifier(o1) === this.getAuthUserIdentifier(o2) : o1 === o2;
  }

  addAuthUserToCollectionIfMissing<Type extends Pick<IAuthUser, 'id'>>(
    authUserCollection: Type[],
    ...authUsersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const authUsers: Type[] = authUsersToCheck.filter(isPresent);
    if (authUsers.length > 0) {
      const authUserCollectionIdentifiers = authUserCollection.map(authUserItem => this.getAuthUserIdentifier(authUserItem));
      const authUsersToAdd = authUsers.filter(authUserItem => {
        const authUserIdentifier = this.getAuthUserIdentifier(authUserItem);
        if (authUserCollectionIdentifiers.includes(authUserIdentifier)) {
          return false;
        }
        authUserCollectionIdentifiers.push(authUserIdentifier);
        return true;
      });
      return [...authUsersToAdd, ...authUserCollection];
    }
    return authUserCollection;
  }
}
