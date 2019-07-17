package dev.jriley.splash

import androidx.lifecycle.ViewModel
import dev.jriley.auth.TokenRepo
import dev.jriley.auth.TokenRepositoryFactory
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber
import java.util.concurrent.TimeUnit

class SplashViewModel(background: Scheduler = Schedulers.io(),
                      tokenRepo: TokenRepo = TokenRepositoryFactory.tokenRepository,
                      private val compositeDisposable: CompositeDisposable = CompositeDisposable(),
                      private val behaviorSubject: BehaviorSubject<Boolean> = BehaviorSubject.create(),
                      val loadingObservable: Observable<Boolean> = behaviorSubject) : ViewModel() {
    init {
        compositeDisposable.add(tokenRepo.isValid()
            .subscribeOn(background)
            .delay(3, TimeUnit.SECONDS, background)
            .doFinally { compositeDisposable.clear() }
            .subscribe({ b -> behaviorSubject.onNext(b) }, { t -> Timber.e(t) }))
    }
}
