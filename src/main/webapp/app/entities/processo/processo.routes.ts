import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ProcessoResolve from './route/processo-routing-resolve.service';

const processoRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/processo.component').then(m => m.ProcessoComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/processo-detail.component').then(m => m.ProcessoDetailComponent),
    resolve: {
      processo: ProcessoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/processo-update.component').then(m => m.ProcessoUpdateComponent),
    resolve: {
      processo: ProcessoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/processo-update.component').then(m => m.ProcessoUpdateComponent),
    resolve: {
      processo: ProcessoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default processoRoute;
