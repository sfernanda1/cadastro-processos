import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProcesso } from '../processo.model';
import { ProcessoService } from '../service/processo.service';

const processoResolve = (route: ActivatedRouteSnapshot): Observable<null | IProcesso> => {
  const id = route.params.id;
  if (id) {
    return inject(ProcessoService)
      .find(id)
      .pipe(
        mergeMap((processo: HttpResponse<IProcesso>) => {
          if (processo.body) {
            return of(processo.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default processoResolve;
